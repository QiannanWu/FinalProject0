/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 7
* Class: CSI4321
*
************************************************/
package instayak.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import instayak.serialization.ComputeHash;
import instayak.serialization.InstaYakACK;
import instayak.serialization.InstaYakChallenge;
import instayak.serialization.InstaYakCredentials;
import instayak.serialization.InstaYakError;
import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakID;
import instayak.serialization.InstaYakMessage;
import instayak.serialization.InstaYakSLMD;
import instayak.serialization.InstaYakUOn;
import instayak.serialization.InstaYakVersion;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * Asynchronous I/O implementation of InstaYak protocol
 * 
 * @version 0.1
 * @author Qiannan Wu
 */
public class InstaYakAIOHandler implements AIOHandler{
	private static Logger logger; // Server logger
	private static HashMap<String, String> passwordMap; //the map stores password
	private static HashMap<String, Integer> seqnoMap; //the map stores password
	private static String expectedMessage; // the expected message type
	public final static String TWO_CHOICE = "SLMD or UOn"; //const for two message type
	private static InstaYakMessage msg; // store a instaYakMessage
    private static String userID; // userID of this user
    private static InstaYakChallenge clng = null; // the challenge created for this used
    private static String password = null; // the password of the user
	
    /**
     * the handler for read
     * 
     * @param readBuff the buffer for reading
     */
	@Override
	public byte[] handleRead(byte[] readBuff) {
		ByteArrayInputStream bis = new ByteArrayInputStream(readBuff);
		MessageInput in = null;
		byte errBuff[] = new byte[1];
		errBuff[0] = -1;
		try {
			in = new MessageInput(bis);
		} catch (NullPointerException e) {
			logger.log(Level.WARNING, "Unable to communicate: <Error in IO>");
			return null;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Unable to communicate: <Error in IO>");
			return null;
		}
		
		InstaYakMessage tmp = getAMessage(in, expectedMessage);
		if(tmp == null){
			return errBuff;
		}
		
		InstaYakMessage respond = createRespond(msg);
		if(respond == null){
			return errBuff;
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		MessageOutput output = new MessageOutput(bos);
		try {
			respond.encode(output);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Unable to communicate: <Error in IO>");
		}
		return bos.toByteArray();
	}
    
	
	/**
	 * Return a InstaYakMessage; if it is a choice between UOn and SLMD, it will update the twitter status
	 * 
	 * @param in
	 *            the MessageInput to get a message
	 * @param expected
	 *            the expected Message type
	 * @return a valid InstaYakMessage or null if there exists error
	 */
	public static InstaYakMessage getAMessage(MessageInput in, String expected) {
		try {
			msg = InstaYakMessage.decode(in);
            
			if(InstaYakError.OPERATION.equals(msg.getOperation())){
				logger.log(Level.WARNING, "Received error: <" + ((InstaYakError)msg).getMessage() + ">");
				return null;
			}
			else if (TWO_CHOICE.equals(expected)) {
				Twitter twitter = new TwitterFactory().getInstance();
				if (InstaYakSLMD.OPERATION.equals(msg.getOperation())) {
					if(seqnoMap.containsKey(userID)){
						Integer lastSeq = seqnoMap.get(userID);
						seqnoMap.replace(userID, lastSeq+1);
					}
					else{
						logger.log(Level.WARNING, "No such user " + userID);
						return null;
					}
					
					String message = userID + ": " + InstaYakSLMD.OPERATION + " " + seqnoMap.get(userID);
					try {
						updateStatus(twitter, message, null);
					} catch (TwitterException e) {
						logger.log(Level.WARNING, "Unable to communicate: <fail to update>");
						return null;
					}
				} else if (InstaYakUOn.OPERATION.equals(msg.getOperation())) {
					String message = userID + ": " + InstaYakUOn.OPERATION + " #" + ((InstaYakUOn) msg).getCategory();
					try {
						updateStatus(twitter, message, ((InstaYakUOn) msg).getImage());
					} catch (TwitterException e) {
						logger.log(Level.WARNING, "Update failed");
						return null;
					}
				}
				else{
					logger.log(Level.WARNING, "Unexpected message: <" + msg.toString() + ">");
					return null;
				}
			}
			else if (!expected.equals(msg.getOperation())) {
				logger.log(Level.WARNING, "Unexpected message: <" + msg.toString() + ">");
				return null;
			} 
			
		} catch (InstaYakException e) {
			logger.log(Level.WARNING, "Invalid message: <" + e.getMessage() + ">");
			return null;
		} catch (IOException e) {
			logger.log(Level.WARNING, "Unable to communicate: <" + e.getMessage() + ">");
			return null;
		}
		return msg;
	}
    
	/**
	 * Create a respond message to a received message
	 * 
	 * @param msg the received message
	 * @return the respond message, null if there exists error
	 */
	public static InstaYakMessage createRespond(InstaYakMessage msg){
		if(InstaYakID.OPERATION.equals(msg.getOperation())){
			// find the ID and password in the password file
			userID = ((InstaYakID) msg).getID();
			if (!passwordMap.containsKey(userID)) {
				InstaYakError err = null;
				try {
					err = new InstaYakError("No such user <" + userID + ">");
				} catch (InstaYakException e) {
					logger.log(Level.WARNING, "Unable to communicate: < Cannot create an error message >");
					return null;
				}
				logger.log(Level.WARNING, err.getMessage());
				return err;
			}
			
			password = passwordMap.get(userID);
			
			// Send Challenge
			try {
				clng = new InstaYakChallenge(Integer.toString(((InstaYakID)msg).hashCode())); //???
				expectedMessage = InstaYakCredentials.OPERATION;
				return clng;
			} catch (InstaYakException e) {
				logger.log(Level.WARNING, "Unable to communicate: < Cannot create a challenge >");
				return null;
			}
		}
		else if(InstaYakCredentials.OPERATION.equals(msg.getOperation())){
			InstaYakCredentials cred = (InstaYakCredentials) msg;

			// Check if the credential can match
			String hashString = null;
			try {
				hashString = ComputeHash.computeHash(clng.getNonce() + password);
			} catch (UnsupportedEncodingException e1) {
				logger.log(Level.WARNING, e1.getMessage());
				return null;
			}
			
			if (!hashString.equals(cred.getHash())) {
				InstaYakError err = null;
				try {
					err = new InstaYakError("Unable to authenticate");
				} catch (InstaYakException e) {
					logger.log(Level.WARNING, "Unable to communicate: < Cannot create a error message >");
					return null;
				}
				logger.log(Level.WARNING, err.getMessage());
				return err;
			}
			else{
				expectedMessage = TWO_CHOICE;
				return new InstaYakACK();
			}
		}
		
		return new InstaYakACK();
	}
	
	
	/**
	 * Post the message and image to twitter
	 * 
	 * @param twitter a twitter
	 * @param message the message needs to be posted
	 * @param img the image needs to be posted
	 * @return the Status of this update
	 * @throws TwitterException if any TwitterException
	 */
	private static Status updateStatus(final Twitter twitter, final String message, final byte[] img)
			throws TwitterException {
		StatusUpdate update = new StatusUpdate(message);
		if (img != null) {

			File f = null;
			try {
				f = File.createTempFile("Img", null, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fos.write(img);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			update.media(f);
		}

		return twitter.updateStatus(update);
	}
	
	/**
	 * Handle accept
	 */
	@Override
	public byte[] handleAccept(){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		MessageOutput output = new MessageOutput(bos);
		// Send version to client
		InstaYakVersion version = new InstaYakVersion();
		try {
			version.encode(output);
		} catch (IOException e) {
		    logger.log(Level.WARNING, "IOException in InstaYakProtocol");
		}
		
		return bos.toByteArray();
	}
	
	
	/**
	 * constructor for InstaYakAIOHandler
	 * 
	 * @param passwordMap the HashMap of password and userID
	 * @param seqnoMap the map of sequence number and userID
	 * @param logger the logger of the server
	 */
	public InstaYakAIOHandler(HashMap<String, String> passwordMap, HashMap<String, Integer> seqnoMap, Logger logger){
		InstaYakAIOHandler.logger = logger;
	    InstaYakAIOHandler.passwordMap = passwordMap;
	    expectedMessage = InstaYakID.OPERATION;
	    InstaYakAIOHandler.seqnoMap = seqnoMap;
	}
}

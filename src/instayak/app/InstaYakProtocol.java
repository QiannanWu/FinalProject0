/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 3
* Class: CSI4321
*
************************************************/
package instayak.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
 * a InstaYak Server handler
 * 
 * @version 1.0 28 Febrary 2017
 * @author Qiannan Wu
 */
public class InstaYakProtocol implements Runnable {
	private Socket clntSock; // Socket connect to client
	private static Logger logger; // Server logger
	private HashMap<String, String> passwordMap; // Map which stores the ID and
													// password
	
	public static final int WAIT_TIME = 60000;
	public final static String TWO_CHOICE = "SLMD or UOn";
    
	/**
	 * Constructor of the InstaYakProtocol
	 * 
	 * @param clntSock the client socket which is connected to
	 * @param logger the logger
	 * @param passwordMap a HashMap which stores the password and user ID information
	 */
	public InstaYakProtocol(Socket clntSock, Logger logger, HashMap<String, String> passwordMap) {
		this.clntSock = clntSock;
		InstaYakProtocol.logger = logger;
		this.passwordMap = passwordMap;
	}

	/**
	 * store a instaYakMessage
	 */
	private static InstaYakMessage msg;

	/**
	 * Return a InstaYakMessage; if it is a choice between UOn and SLMD, it will update the twitter status
	 * 
	 * @param in
	 *            the MessageInput to get a message
	 * @param expected
	 *            the expected Message type
	 * @param userID the user's ID
	 * @return a valid InstaYakMessage
	 */
	public static InstaYakMessage getAMessage(MessageInput in, String expected, String userID) {
		try {
			msg = InstaYakMessage.decode(in);

			if (TWO_CHOICE.equals(expected)) {
				Twitter twitter = new TwitterFactory().getInstance();
				if (InstaYakSLMD.OPERATION.equals(msg.getOperation())) {
					if(InstaYakServer.seqnoMap.containsKey(userID)){
						Integer lastSeq =InstaYakServer. seqnoMap.get(userID);
						InstaYakServer.seqnoMap.replace(userID, lastSeq+1);
					}
					else{
						System.out.println("Cannot find userID " + userID);
					}
					
					
					String message = userID + ": " + InstaYakSLMD.OPERATION + " " + InstaYakServer.seqnoMap.get(userID);
					try {
						updateStatus(twitter, message, null);
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (InstaYakUOn.OPERATION.equals(msg.getOperation())) {
					String message = userID + ": " + InstaYakUOn.OPERATION + " #" + ((InstaYakUOn) msg).getCategory();
					try {
						updateStatus(twitter, message, ((InstaYakUOn) msg).getImage());
					} catch (TwitterException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			else if (!expected.equals(msg.getOperation())) {
				logger.log(Level.WARNING, "Unexpected message: <" + msg.toString() + ">");
				return null;
			} else {
				switch (expected) {
				case InstaYakVersion.OPERATION:
					System.out.println(msg.getOperation() + " " + ((InstaYakVersion) msg).getVersion());
					break;
				case InstaYakChallenge.OPERATION:
					System.out.println(msg.getOperation() + " " + ((InstaYakChallenge) msg).getNonce());
					break;
				case InstaYakACK.OPERATION:
					System.out.println(msg.getOperation());
				default:
					break;
				}
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
	 * handler for the client
	 * 
	 * @param clntSock the client socket
	 * @param logger the logger
	 * @param passwordMap a HashMap which stores the user ID and password information
	 */
	public static void handleClient(Socket clntSock, Logger logger, HashMap<String, String> passwordMap) {
		try {
			// Get the input and output I/O streams from socket
			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();

			System.out.println("Handling client <" + clntSock.getLocalAddress() + ">-<" + clntSock.getLocalPort()
					+ "> with thread id <" + Thread.currentThread().getId() + ">");

			MessageInput input = null;
			try {
				input = new MessageInput(in);
			} catch (NullPointerException e) {
				logger.log(Level.WARNING, "Unable to communicate: <Error in IO>");
				return;
			}

			MessageOutput output = new MessageOutput(out);

			// Send version to client
			InstaYakVersion version = new InstaYakVersion();
			version.encode(output);

			// Receive ID
			if (getAMessage(input, InstaYakID.OPERATION, null) == null) {
				return;
			}
			InstaYakID userid = (InstaYakID) msg;

			// find the ID and password in the password file
			if (!passwordMap.containsKey(userid.getID())) {
				InstaYakError err = null;
				try {
					err = new InstaYakError("No such user <" + userid.getID() + ">");
				} catch (InstaYakException e) {
					logger.log(Level.WARNING, "Unable to communicate: < Cannot create a error message >");
					return;
				}
				logger.log(Level.WARNING, err.getMessage());
				err.encode(output);
				return;
			}

			String password = passwordMap.get(userid.getID());
			InstaYakChallenge clng = null;
			// Send Challenge
			try {
				clng = new InstaYakChallenge(Integer.toString(userid.hashCode()));
				clng.encode(output);
			} catch (InstaYakException e) {
				logger.log(Level.WARNING, "Unable to communicate: < Cannot create a challenge >");
				return;
			}

			// Receive CRED
			if (getAMessage(input, InstaYakCredentials.OPERATION, null) == null) {
				return;
			}

			InstaYakCredentials cred = (InstaYakCredentials) msg;

			// Check if the credential can match
			String hashString = ComputeHash.computeHash(clng.getNonce() + password);
			if (!hashString.equals(cred.getHash())) {
				InstaYakError err = null;
				try {
					err = new InstaYakError("Unable to authenticate");
				} catch (InstaYakException e) {
					logger.log(Level.WARNING, "Unable to communicate: < Cannot create a error message >");
					return;
				}
				logger.log(Level.WARNING, err.getMessage());
				err.encode(output);
				return;
			}
			// Send ACK
			InstaYakACK ACK = new InstaYakACK();
			ACK.encode(output);

			// Receive UOn or SLMD, and send ACK after post
			while (true) {
				long startTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - startTime < WAIT_TIME && !input.hasNext()) {

				}

				if (input.hasNext()) {
                    getAMessage(input, TWO_CHOICE, userid.getID());
				} else {// timeout
                    //System.out.println("time out");
					return;
				}
			}

		} catch (IOException ex) {
			logger.log(Level.WARNING, "Exception in InstaYakProtocol", ex);
		} finally {
			try {
				clntSock.close();
			} catch (IOException e) {

			}
		}
	}

	@Override
	public void run() {
		handleClient(clntSock, logger, passwordMap);
	}

}

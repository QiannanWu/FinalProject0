/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 2
* Class: CSI4321
*
************************************************/
package instayak.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Scanner;

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

/**
 * a InstaYak Client which can communicate with a specific server
 * 
 * @version 1.0 13 Febrary 2017
 * @author Qiannan Wu
 */
public class InstaYakClient {
	/**
	 * the socket created for communication
	 */
	private static Socket socket;

	/**
	 * the scanner for user's request
	 */
	private static Scanner scanner;

	/**
	 * total number of parameter
	 */
	private final static int numberOfParameter = 4;

	/**
	 * index of server in args
	 */
	private final static int servIndex = 0;

	/**
	 * index of port in args
	 */
	private final static int portIndex = 1;

	/**
	 * index of userid in args
	 */
	private final static int useridIndex = 2;

	/**
	 * index of password in args
	 */
	private final static int passwordIndex = 3;

	/**
	 * store a instaYakMessage
	 */
	private static InstaYakMessage msg;

	/**
	 * Return a InstaYakMessage. If it is InstaYakError, output error
	 * information and then terminate; if it is not the expected message type,
	 * output the information; if not valid, output invalid information and then
	 * terminate.
	 * 
	 * @param in
	 *            the MessageInput to get a message
	 * @param expected
	 *            the expected Message type
	 * @return a valid InstaYakMessage
	 */
	public static InstaYakMessage getAMessage(MessageInput in, String expected) {
		try {
			msg = InstaYakMessage.decode(in);
			if (msg.getOperation().equals(InstaYakError.OPERATION)) {
				System.err.println("Error: <" + ((InstaYakError) msg).getMessage() + ">");
				System.exit(1);
			}

			if (!expected.equals(msg.getOperation())) {
				System.err.println("Unexpected message: <" + msg.toString() + ">");
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
			System.err.println("Invalid message: <" + e.getMessage() + ">");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Unable to communicate: <" + e.getMessage() + ">");
			System.exit(1);
		}
		return msg;
	}
 
	/**
	 * The main method for InstaYakClient
	 * 
	 * @param args
	 *            the parameter passed to the method
	 * @throws UnsupportedEncodingException
	 *             if UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {

		// Check if has correct number of parameters
		if (args.length != numberOfParameter) {
			throw new IllegalArgumentException("Parameter(s): <server> <port> <userid> <password>");
		}
		try {
			String server = args[servIndex]; // Server name or IP address
			int servPort = Integer.parseInt(args[portIndex]); // get Port
			String ID = args[useridIndex]; // get userid
			String password = args[passwordIndex]; // get password

			socket = new Socket(server, servPort);

			InputStream in = null;
			in = socket.getInputStream();

			OutputStream out = null;
			out = socket.getOutputStream();

			MessageInput input = null;
			try {
				input = new MessageInput(in);
			} catch (NullPointerException e) {
				System.err.println("Unable to communicate: <Error in IO>");
				System.exit(1);
			}

			MessageOutput output = new MessageOutput(out);

			// Receive an InstaYak message
			do {
				getAMessage(input, InstaYakVersion.OPERATION);
			} while (!InstaYakVersion.OPERATION.equals(msg.getOperation()));

			// Send ID to server
			try {
				InstaYakID id = new InstaYakID(ID);
				id.encode(output);
			} catch (InstaYakException e) {
				System.err.println("Validation failed: <" + e.getMessage() + ">");
				System.exit(1);
			}

			// Receive a CLNG
			do {
				getAMessage(input, InstaYakChallenge.OPERATION);
			} while (!InstaYakChallenge.OPERATION.equals(msg.getOperation()));

			InstaYakChallenge challenge = (InstaYakChallenge) msg;

			// Send the CRED to server
			String hashString = ComputeHash.computeHash(challenge.getNonce() + password);
			try {
				InstaYakCredentials cred = new InstaYakCredentials(hashString);
				cred.encode(output);
			} catch (InstaYakException e) {
				System.err.println("Validation failed: <Cannot create credential>");
				System.exit(1);
			}

			// Client sends post requests
			scanner = new Scanner(System.in);
			String choice;
			do {
				choice = scanner.next();
				switch (choice) {
				case InstaYakUOn.OPERATION:
					FileInputStream imgFile = null;
					boolean invalidFile = true;
					byte[] image = null;
					while (invalidFile) {
						try {
							String category = scanner.next();
							imgFile = new FileInputStream(scanner.next());
							image = new byte[imgFile.available()];
							imgFile.read(image);
							new InstaYakUOn(category, image).encode(output);
							invalidFile = false;
						} catch (FileNotFoundException e2) {
							System.err.println("Validation failed: <Cannot find the image, retry filename>");
						} catch (IOException e2) {
							System.err.println("Validation failed: <Cannot read the image file, try another file>");
						} catch (InstaYakException e) {
							System.err.println("Validation failed: <" + e.getMessage() + ", retry>");
						}
					}
					break;
				case InstaYakSLMD.OPERATION:
					new InstaYakSLMD().encode(output);
					break;
				default:
					System.err.println("Validation failed: <Invalid quest>");
					break;
				}

				// Receive a ACK
				do {
					getAMessage(input, InstaYakACK.OPERATION);
				} while (!InstaYakACK.OPERATION.equals(msg.getOperation()));

				choice = scanner.next();
			} while ("y".equals(choice) || "Y".equals(choice));

			socket.close();

		} catch (IOException e) {
			System.err.println("Unable to communicate: <" + e.getMessage() + ">");
			System.exit(1);
		}
	}
}

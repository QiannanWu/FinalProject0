/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 7
* Class: CSI4321
*
************************************************/
package instayak.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

import instayak.serialization.InstaYakMessage;

/**
 * InstaYak server using asynchronous I/O
 * 
 * @version 0.1
 * @author Qiannan Wu
 */
public class InstaYakServerAIO {

	/**
	 * Logger for server
	 */
	protected static final Logger logger = Logger.getLogger("InstaYakServerAIO");
	/**
     * A Scanner for read in the password and user information from file
     */
	private static Scanner sin;
	/**
	 * A HashMap which stores the user ID and password information
	 */
	private static HashMap<String, String> passwordMap;
	
	/**
	 * A HashMap which sorers the user ID and sequence number
  	 */
	public static HashMap<String, Integer> seqnoMap;
	
	// Configure logger handler (connections.log) and format (simple)
	static {
		logger.setUseParentHandlers(false);
		try {
			Handler handler = new FileHandler("connections.log");
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (Exception e) {
			System.err.println("Unable to initialized logger");
			System.exit(1);
		}
	}
    
	/**
	 * The main method of InstaYak AIO server
	 * 
	 * @param args the arguments passed to main
	 */
	public static void main(final String[] args) {
		// Test for args correctness and process
		if (args.length != 2) {
			throw new IllegalArgumentException("Parameter(s): <Port> <Password File>");
		}
		// Local server port
		int port = Integer.parseInt(args[0]);
        
		//password file
		FileInputStream passwordFile =  null;
    	passwordMap = new HashMap<String, String>();
    	seqnoMap = new HashMap<String, Integer>();
    	
    	try{
    	    passwordFile = new FileInputStream(args[1]);
    	    
    	    sin = new Scanner(passwordFile, InstaYakMessage.PROTOCOL);
    	    sin.useDelimiter(Pattern.compile("\r\n"));
    	    Integer zero = new Integer(0);
    	    while(sin.hasNext()){
    	    	String line = sin.next();
    	    	String delim = ":";
    	    	String []tokens = line.split(delim);
    	    	passwordMap.put(tokens[0], tokens[1]);
    	    	seqnoMap.put(tokens[0], zero);
    	    }    
    	}catch(NumberFormatException e){
    		System.err.println("Parameter(s) format error");
    		System.exit(1);
    	}catch(FileNotFoundException e2){
    		System.err.println("Password file not found");
    		System.exit(1);
    	}
    	
		// Create listening socket channel
		AsynchronousServerSocketChannel listenChannel = null;
		try {
			// Bind local port
			listenChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
			// Create accept handler
			listenChannel.accept(null, makeAcceptCompletionHandler(listenChannel, logger));
		} catch (IOException ex) {
			System.err.println("Unable to create server socket channel: " + ex.getMessage());
			System.exit(1);
		}
		// Block until current thread dies
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Create completion handler for accept
	 * 
	 * @param listenChannel
	 *            channel listening for new clients
	 * @param logger
	 *            server logger
	 * 
	 * @return completion handler
	 */
	public static CompletionHandler<AsynchronousSocketChannel, Void> makeAcceptCompletionHandler(
			final AsynchronousServerSocketChannel listenChannel, final Logger logger) {
		return new CompletionHandler<AsynchronousSocketChannel, Void>() {
			/**
			 * Called when accept completes
			 * 
			 * @param clntChan channel for new client
			 * 
			 * @param v void means no attachment
			 */
			@Override
			public void completed(AsynchronousSocketChannel clntChan, Void v) {
				logger.info("Handling accept for " + clntChan);
				listenChannel.accept(null, this);
				TCPAIODispatcher aioDispatcher = new TCPAIODispatcher(new InstaYakAIOHandler(passwordMap, seqnoMap, logger), logger);
				try {
					aioDispatcher.handleAccept(clntChan);
				} catch (IOException e) {
				}
				
			}

			/**
			 * Called if accept fails
			 * 
			 * @param ex exception triggered by accept failure
			 * 
			 * @param v void means no attachment
			 */
			@Override
			public void failed(Throwable ex, Void v) {
				logger.log(Level.WARNING, "accept failed", ex);
			}
		};
	}
}
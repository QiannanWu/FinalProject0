/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 3
* Class: CSI4321
*
************************************************/
package instayak.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import instayak.serialization.InstaYakMessage;

/**
 * a InstaYak Server pool
 * 
 * @version 1.0 28 Febrary 2017
 * @author Qiannan Wu
 */
public class InstaYakServer {
	
	/**
	 * A server socket to accept client connection requrests
	 */
    private static ServerSocket servSock;
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
	
	/**
	 * the main method for server pool
	 * 
	 * @param args the arguments passed to main
	 * @throws IOException If I/O problem
	 */
	public static void main(String[] args) throws IOException{
    	
    	if(args.length != 3) {// Test for correct number of args
    		throw new IllegalArgumentException("Parameter(s):<Port> <Thread> <Password File>");
    	}
    	
    	int servPort = 0;
    	int threadPoolSize = 0;
    	FileInputStream passwordFile = null;
    	passwordMap = new HashMap<String, String>();
    	seqnoMap = new HashMap<String, Integer>();
    	try{
    	    servPort = Integer.parseInt(args[0]);
    	    threadPoolSize = Integer.parseInt(args[1]);
    	    passwordFile = new FileInputStream(args[2]);
    	    
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
    	
    	servSock = new ServerSocket(servPort);
    	
    	final Logger logger = Logger.getLogger("practical");
    	
    	//Spawn a fixed number of threads to service clients
    	for(int i = 0; i < threadPoolSize; ++i){
    		Thread thread = new Thread(){
    			public void run(){
    				while(true){
    					try {
    						Socket clntSocket = servSock.accept(); //wait for a connection
    						InstaYakProtocol.handleClient(clntSocket, logger, passwordMap);
    					}catch(IOException ex){
    						logger.log(Level.WARNING, "Unable to communicate: ", ex);
    					}
    				}
    			}
    		};
    		thread.start();
    		//logger.info("Created and started Thread = " + thread.getName());
    	}
    }
}

/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 5
* Class: CSI4321
*
************************************************/
package hoon.app;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Random;

import hoon.serialization.ErrorCode;
import hoon.serialization.HoOnException;
import hoon.serialization.HoOnMessage;
import hoon.serialization.HoOnQuery;
import hoon.serialization.HoOnResponse;

/**
 * a HoOn Client which can communicate with a specific server
 * 
 * @version 1.0 29 March 2017
 * @author Qiannan Wu
 */
public class HoOnClient {
	
	private static DatagramSocket socket; // the socket for sending and receiving
    private static final int PACKET_SIZE = 65535; // the maximum size of packet
    private static final int TIMEOUT = 3000; // time out for this client
    private static final int MAXTRIES = 5; // the maximum times for this client to try
    
    /**
     * get a HoOnResponse from the given buffer
     * 
     * @param buffer the buffer contains the data
     * 
     * @return it returns the HoOnResponse
     *  
     * @throws IOException if I/O problem
     */
    public static HoOnResponse getResponse(byte[] buffer) throws IOException{
    	HoOnMessage r = null;
    	try{
	    	r = HoOnMessage.decode(buffer);
	    	
	    	if((HoOnQuery.TYPE).equals(r.getType())){
	    		System.err.println((ErrorCode.BADVERSION).getErrorMessage());
	    	    System.exit(-1);
	    	}
	    	
	    	if(!ErrorCode.NOERROR.equals(r.getErrorCode())){
	    		System.err.println(r.getErrorCode().getErrorMessage());
	    		System.exit(-1);
	    	}
	    	
	    }catch (HoOnException e){
	    	if((ErrorCode.BADVERSION).getErrorMessage().equals(e.getMessage())){
	    	    System.err.println((ErrorCode.BADVERSION).getErrorMessage());
	    	    System.exit(-1);
	    	}
	    	
	    	if((ErrorCode.NETWORKERROR).getErrorMessage().equals(e.getMessage())){
	    	    System.err.println("Non-zero reserve");
	    	    System.exit(-1);
	    	}
	    }
    	
    	return (HoOnResponse)r;
    }
    
    /**
     * Receive a packet from a socket
     * 
     * @param s the socket for receiving the packet
     * 
     * @return it returns the byte[] array that received
     * @throws IOException if I/O exception
     */
    public static byte[] receivePacket(DatagramSocket s) throws IOException{
    	DatagramPacket r = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
    	s.receive(r);
    	int receivedLength = r.getLength();
		byte[] buffer = r.getData();
		byte[] b = Arrays.copyOf(buffer, receivedLength);
		
		return b;
    }
    
    /**
     * Create a HoOnQuery with the given number of posts
     * 
     * @param numberOfPosts the number of posts required
     * @return it returns the created HoOnQuery
     */
    public static HoOnQuery createQuery(int numberOfPosts){
    	long randomID;
		Random rd = new Random();
		
		do{
			randomID = rd.nextInt();
		}while(!HoOnMessage.isLegalId(randomID));
		
		HoOnQuery query = new HoOnQuery(randomID, numberOfPosts);
		return query;
    }
    
    /**
     * the start of a HoOnClient
     * 
     * @param args the parameter passed to the client
     * @throws IOException if I/O exception
     */
	public static void main(String[] args) throws IOException{
		if(args.length != 3){ // Check if has correct number of parameters
			throw new IllegalArgumentException("Parameter(s): <Server IP/name> <Server port> <number of responses>");	
		}
		
		InetAddress serverAddress = InetAddress.getByName(args[0]);
		int servPort = Integer.parseInt(args[1]);
		int numberOfPosts = Integer.parseInt(args[2]);
		
		socket = new DatagramSocket();
		socket.setSoTimeout(TIMEOUT);
		
		HoOnQuery query = createQuery(numberOfPosts);
		byte[] bytesToSend = query.encode();
		DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);
		
		int tries = 0;
		boolean receivedResponse = false;
		HoOnResponse response = null;
		do{
			try{
				socket.send(sendPacket);
			    byte[] buffer = receivePacket(socket);	
		        response = getResponse(buffer);
		        if(response.getQueryId() != query.getQueryId()){
		    	    continue;
		        } 
		        receivedResponse = true;
		    }catch (InterruptedIOException e){
		    	tries++;
		    }
			
		}while(!receivedResponse && (tries < MAXTRIES));
		
		if(receivedResponse && response != null){
			System.out.println(response.toString());
		}
		
		socket.close();
	}
}

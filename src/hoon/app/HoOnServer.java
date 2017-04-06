/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 6
* Class: CSI4321
*
************************************************/
package hoon.app;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

import hoon.serialization.ErrorCode;
import hoon.serialization.HoOnException;
import hoon.serialization.HoOnMessage;
import hoon.serialization.HoOnQuery;
import hoon.serialization.HoOnResponse;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 * a HoOn Server which can receive queries and send back responses 
 * 
 * @version 1.0 6 April 2017
 * @author Qiannan Wu
 */
public class HoOnServer {
	
	private static final Logger LOGGER = Logger.getLogger(HoOnServer.class.getName()); // the logger for HoOnServer
	public static final int PACKET_SIZE = 65535; // the maximum size of packet
	public static final int LENGTH_BYTE = 2; // the size of length of each post
	
	/**
     * get a HoOnQuery from the given buffer
     * 
     * @param buffer the buffer contains the data
     * 
     * @return it returns the HoOnResponse
     *  
     * @throws IOException if I/O problem
	 * @throws HoOnException if HoOnException
     */
    public static HoOnQuery getQuery(byte[] buffer) throws IOException, HoOnException{
    	HoOnMessage r = null;
    
	    r = HoOnMessage.decode(buffer);
	    	
	    if((HoOnResponse.TYPE).equals(r.getType())){
	    	throw new HoOnException(ErrorCode.UNEXPECTEDPACKETTYPE);
	    }
	    	
	    if(!ErrorCode.NOERROR.equals(r.getErrorCode())){
	    	throw new HoOnException(ErrorCode.UNEXPECTEDERRORCODE);
	    }    
    	
    	return (HoOnQuery)r;
    }
	
	
    /**
     * Receive a packet from a socket
     * 
     * @param s the socket for receiving the packet
     * @param r the packet for receiving data
     * 
     * @return it returns the byte[] array that received
     * @throws IOException if I/O exception
     */
    public static byte[] receivePacket(DatagramSocket s, DatagramPacket r) throws IOException{
    	s.receive(r);
    	int receivedLength = r.getLength();
		byte[] buffer = r.getData();
		byte[] b = Arrays.copyOf(buffer, receivedLength);
		return b;
    }
    
    public static List<String> getTwitter(int n) throws TwitterException{
    	new TwitterFactory();
		Twitter twitter = TwitterFactory.getSingleton();
		int totalSize = LENGTH_BYTE;
		Paging paging = new Paging(1, n);
		List<Status> statuses = twitter.getHomeTimeline(paging);
		List<String> posts = new ArrayList<String>();
		
		for(Status status : statuses){
			String tmp = status.getText();
			if(totalSize + tmp.length() + LENGTH_BYTE <= HoOnMessage.MAX_SIZE_RESPONSE - HoOnMessage.RESPONSE_HEADER){
				totalSize += tmp.length() + LENGTH_BYTE;
				posts.add(tmp);
			}
			else{
			    break;
			}
		}
		
		return posts;
    }
    
    /**
     * Create a HoOnResponse for a query
     * 
     * @param q the query
     * @return the response to the query
     */
    public static HoOnResponse createResponse(HoOnQuery q){
    	int numberRequested = q.getRequestedPosts();
    	try {
			List<String> posts = getTwitter(numberRequested);
			return new HoOnResponse(ErrorCode.NOERROR, q.getQueryId(), posts);
		} catch (TwitterException e) {
			LOGGER.log(Level.WARNING, e.getMessage(), e);
			return new HoOnResponse(ErrorCode.NETWORKERROR, q.getQueryId(), new ArrayList<String>());
		}
    }
    
    
	/**
     * the start of a HoOnServer
     * 
     * @param args the parameter passed to the server
	 * @throws IOException if I/O exception
     */
    public static void main(String[] args) throws IOException{
    	if(args.length != 1){
    		throw new IllegalArgumentException("Parameter(s): <Port>");
    	}
    	
    	FileHandler fh = new FileHandler("connrctions.log");
    	LOGGER.addHandler(fh);
    	int servPort = Integer.parseInt(args[0]);
    	DatagramSocket socket = new DatagramSocket(servPort);
		DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
		
    	while(true){
			try {	
				byte[] buffer = receivePacket(socket, packet);
				LOGGER.log(Level.SEVERE, "Sourse Address: " +  packet.getAddress() + " / Port: " + packet.getPort());
				
				HoOnQuery query = getQuery(buffer);
				HoOnResponse response = createResponse(query);
				byte[] bytesToSend = response.encode();
				DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, packet.getAddress(), packet.getPort());
				socket.send(sendPacket);
				
			}catch (IOException e) {
			    LOGGER.log(Level.WARNING, e.getMessage(), e);
		    }catch (HoOnException e){
			    LOGGER.log(Level.WARNING, e.getMessage(), e);
			    HoOnResponse response = new HoOnResponse(e.getErrorCode(), 0, new ArrayList<String>());
			    byte[] bytesToSend = response.encode();
			    DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, packet.getAddress(), packet.getPort());
			    socket.send(sendPacket);
		    }
    	
    	    packet.setLength(PACKET_SIZE);
    	}
    }
}

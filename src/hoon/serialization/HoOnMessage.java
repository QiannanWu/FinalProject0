/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a HoOn message header and performs wire serialization/deserialization
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public abstract class HoOnMessage {
	
	public static final byte QUERY_HEADER = 0x32; // first byte of query header
	public static final byte RESPONSE_HEADER = 0x40; // QR of response of response header
	private static final int MIN_SIZE = 8; // minimum size of a query packet
	private static final int MAX_SIZE_QUERY = 8; // max size of a query packet
	private static final int MAX_SIZE_RESPONSE = 65507; // max size of a response packet (UDP payload size)
	private static final byte MASK_VERSION = 0x32; // the mask for right version
	private static final int ERROR_CODE_RANGE = 7; // the range of error code
	private static final byte MASK_RESERVE = 0x7; // the mask for checking reserve bits
	public static final int MAX_POST_NUMBER = 65535; // the max number of posts
	public static final int MIN_POST_NUMBER = 0; // the minimum number of posts
	public static final long MAX_ID = 4294967295L; // the max value of a ID
	public static final long MIN_ID = 0; // the minimum value of a ID
	public static final int MAX_POST_LENGTH = 65535; // the max value of a post length
	public static final int MIN_POST_LENGTH = 0; // the minimum value of a post length
	public static final int ID_LENGTH = 4; // the length of a query ID
	public static final int NUMBER_POST_LENGTH = 2; // the length of number of posts
    /**
     * Check if the first byte contains the correct version
     * 
     * @param b the first byte of header
     * @return It returns true if the version is correct; otherwise false
     */
    public static boolean checkVersion(byte b){
    	if((b & MASK_VERSION) == MASK_VERSION){
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * Check if the error code is valid
     * 
     * @param errorCode the error code
     * @return it returns true if the error code is valid; otherwise false
     */
    public static boolean checkErrorCode(int errorCode){
    	if(errorCode > ERROR_CODE_RANGE){
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Check if the first byte contains the correct reserved bits
     * 
     * @param b the first byte of header
     * @return It returns true if the reserved bits is correct; otherwise false
     */
    public static boolean checkReserved(byte b){
    	if((b & MASK_RESERVE) == 0x0){
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * check if the first 2 byte of the header is correct
     * 
     * @param buffer bytes from which to deserialize
     * 
     * @return It returns true if it passes the check
     * @throws HoOnException if there are errors in the header
     */
    public static boolean checkHeader(byte firstByte, int errorCodeValue) throws HoOnException{
    	
    	if(!checkVersion(firstByte)){
    		throw new HoOnException(ErrorCode.BADVERSION);
    	}
    	
    	if(!checkErrorCode(errorCodeValue)){
    		throw new HoOnException(ErrorCode.UNEXPECTEDERRORCODE);
    	}
    	
    	if(!checkReserved(firstByte)){
    		throw new HoOnException(ErrorCode.NETWORKERROR);
    	}
    	
    	return true;
    }
    
    
    /**
     * Deserialize HoOn Message header.
     * 
     * @param buffer bytes from which to deserialize
     * 
     * @return Deserialized HoOn message
     * @throws HoOnException if deserialization or validation fails
     */
    public static HoOnMessage decode(byte[] buffer) throws HoOnException{
    	if(buffer == null || buffer.length < MIN_SIZE){
    		throw new HoOnException(ErrorCode.PACKETTOOSHORT);
    	}
    	
    	byte firstByte = buffer[0];
     	int errorCodeValue = buffer[1];
     	long queryId = 0;
     	for (int i = ID_START; i < ID_END; i++)
     	{
     	   queryId = (queryId << 8) + (buffer[i] & 0xff);
     	}
     	
     	checkHeader(firstByte, errorCodeValue);
     	ErrorCode errorCode = ErrorCode.getErrorCode(errorCodeValue);
     	
    	if(firstByte == QUERY_HEADER){
    		if(buffer.length > MAX_SIZE_QUERY){
    			throw new HoOnException(ErrorCode.PACKETTOOLONG);
    		}
    		
    		if(!ErrorCode.NOERROR.equals(errorCodeValue)){
    			throw new HoOnException(ErrorCode.UNEXPECTEDERRORCODE);
    		}
    		
    		int requestedPosts = 0;
    		
    		for (int i = NUMBER_POST_START; i < NUMBER_POST_END; i++)
         	{
    			requestedPosts = (requestedPosts << 8) + (buffer[i] & 0xff);
         	}
    		
    		return new HoOnQuery(queryId, requestedPosts);
    	}
    	else if(firstByte == RESPONSE_HEADER){
    		if(buffer.length > MAX_SIZE_RESPONSE){
    			throw new HoOnException(ErrorCode.PACKETTOOLONG);
    		}
            int numberOfPosts = 0;
    		List<String> posts = new ArrayList<String>();
    		
    		for (int i = NUMBER_POST_START; i < NUMBER_POST_END; i++)
         	{
    			numberOfPosts = (numberOfPosts << 8) + (buffer[i] & 0xff);
         	}
    		
    		int cur = NUMBER_POST_END;
    		for(int i = 0; i < numberOfPosts; ++i){
    			int postLength = 0;
    		}
    		return new HoOnResponse();
    	}
    	
    	throw new HoOnException(ErrorCode.UNEXPECTEDPACKETTYPE);
    }
    
    /**
     * Set the message query ID
     * 
     * @param queryId the new query ID
     * @throws IllegalAccessException if the given ID is out of range
     */
    public abstract void setQueryId(long queryId) throws IllegalAccessException;
    
    /**
     * Get tge nessage query ID
     * 
     * @return current query ID
     */
    public abstract long getQueryId();
    
    /**
     * Serialize the HoOn message
     * 
     * @return serialized HoOn message
     * @throws HoOnException if error during serialization
     */
    public abstract byte[] encode() throws HoOnException;
    
    /**
     * Get the message error code
     * 
     * @return message error code
     */
    public ErrorCode getErrorCode(){
    	return errorCode;
    }
    
	/**
	 * Check if the requestedPosts is outside of the range
	 * @param requestedPosts the number of posts
	 * @return It returns true if the requestedPosts is legal; otherwise false.
	 */
	public boolean isLegalPostsNumber(int requestedPosts) {
		if(requestedPosts < MIN_POST_NUMBER || requestedPosts > MAX_POST_NUMBER){
			return false;
		}
		
		return true;
	}
    
	/**
	 * Check if the  query id is outside of range
	 * 
	 * @param queryId the query ID
	 * @return It returns true if the queryId is legal; otherwise false
	 */
	public boolean isLegalId(long queryId) {
		if(queryId > MAX_ID || queryId < MIN_ID){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if a post is outside length range
	 * 
	 * @param post the post message
	 * @return It returns true if the length is outside length range
	 */
	public boolean isLegalPost(String post){
		if(post.length() > MAX_POST_LENGTH || post.length() < MIN_POST_LENGTH){
			return false;
		}	
		return true;
	}
	
	/**
     * Returns type of the packet
     * 
     * @return packet type
     */
    public abstract String getType();
}
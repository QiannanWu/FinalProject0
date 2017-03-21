/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization;

/**
 * Represents a HoOn query and performs wire serialization/deserialization
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public class HoOnQuery extends HoOnMessage{
    private int requestedPosts; //the maximum number of posts to return
    private static String TYPE = "QUERY"; //represent the query type 
    private long queryId; // current query ID
	
	/**
	 * Deserialize HoOn query
	 * @param buffer bytes from which to deserialize
	 * @throws if deserialization fails
	 */
	HoOnQuery(byte[] buffer) throws HoOnException{
		HoOnMessage message = HoOnMessage.decode(buffer);
    	if(message.getType() != TYPE){
    		throw new HoOnException(ErrorCode.UNEXPECTEDPACKETTYPE);
    	}
    	
    	queryId = ((HoOnQuery)message).getQueryId();
    	requestedPosts = ((HoOnQuery)message).getRequestedPosts();
	}
	
	/**
	 * Creates a new HoOn query given individual elements
	 * 
	 * @param queryId ID for query
	 * @param requestedPosts Number of requested posts
	 * @throws IllegalArgumentException If the queryId or requestedPosts are outside the allowable range
	 */
	public HoOnQuery(long queryId, int requestedPosts) throws IllegalArgumentException{
		if(!isLegalId(queryId) || !isLegalPostsNumber(requestedPosts)){
			throw new IllegalArgumentException("Parameters are outside of allowable range");
		}
		this.queryId = queryId;
		this.requestedPosts = requestedPosts;
	}
	
	/**
	 * Returns the information from the query
	 * @return the information from the query
	 */
	@Override
	public String toString(){
		return "[query ID]: " + queryId + ", [maximum number of posts]: " + requestedPosts;
	}
    
	/**
	 * Get the number of requested posts in the message
	 * 
	 * @return current number of requested posts
	 */
	public int getRequestedPosts(){
		return requestedPosts;
	}
	
	/**
	 * Set the number of requested posts in the message
	 * 
	 * @param requestedPosts new number of requested posts
	 * @throws IllegalArgumentException if number of requested posts out of range
	 */
	public void setRequestedPosts(int requestedPosts) throws IllegalArgumentException{
		if(!isLegalPostsNumber(requestedPosts)){
			throw new IllegalArgumentException("Number of posts out of range");
		}
		
		this.requestedPosts = requestedPosts;
	}
	
	 /**
     * Set the message query ID
     * 
     * @param queryId the new query ID
     * @throws IllegalAccessException if the given ID is out of range
     */
    public void setQueryId(long queryId) throws IllegalAccessException{
    	this.queryId = queryId;
    }
	
    /**
     * Get the response ID
     * 
     * @return current query ID
     */
    public long getQueryId(){
    	return queryId;
    }
    
	/**
     * Serialize the HoOnQuery
     * 
     * @return serialized HoOn Query
     * @throws HoOnException if error during serialization
     */
	@Override
	public byte[] encode() throws HoOnException {
		
	}
    
	/**
     * Returns type of the packet
     * 
     * @return packet type
     */
	@Override
	public String getType() {
		return TYPE;
	}
}

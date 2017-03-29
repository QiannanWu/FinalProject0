/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Represents a HoOn response and performs wire serialization/deserialization
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public class HoOnResponse extends HoOnMessage{
    private List<String> posts; //posts list of posts
    private ErrorCode errorCode; // the error code
    private long queryId; // current query ID
	private static String TYPE = "RESPONSE"; //represent the response type
	
	/**
	 * Deserialize HoOn response
	 * 
	 * @param buffer bytes from which to deserialize
	 * @throws IOException if I/O exception
	 */
	public HoOnResponse(byte[] buffer) throws HoOnException, IOException{
    	HoOnMessage message = HoOnMessage.decode(buffer);
    	if(message.getType() != TYPE){
    		throw new HoOnException(ErrorCode.UNEXPECTEDPACKETTYPE);
    	}
    	
    	posts = ((HoOnResponse)message).getPosts();
    	queryId = ((HoOnResponse)message).getQueryId();
    	errorCode = ((HoOnResponse)message).getErrorCode();
    }
	
	/**
	 * Creates a new HoOn response given individual elements
	 * 
	 * @param errorCode error code for response
	 * @param queryId ID for response
	 * @param posts list of posts
	 * @throws IllegalArgumentException if the queryId or post list are out side the allowable range
	 */
	public HoOnResponse(ErrorCode errorCode, long queryId, List<String> posts) throws IllegalArgumentException{
		if(posts == null || !isLegalId(queryId) || !isLegalPostsNumber(posts.size())){
			throw new IllegalArgumentException("Illegal response");
		}
		
		for(String s : posts){
			if(!isLegalPost(s)){
				throw new IllegalArgumentException("Post too long");
			}
		}
		
		this.errorCode = errorCode;
		this.queryId = queryId;
		this.posts = posts;
	}
	
	/**
	 * Returns the information from the response
	 * @return the information from the response
	 */
	public String toString(){
		String s = "[error code]: " + errorCode.getErrorMessage() + ", [query ID]: " + queryId + "\n[posts]:";
		for(String post : posts){
			s += post + "\n";
		}
		return s;
	}
	
	/**
	 * Get the response list of posts
	 * 
	 * @return current list of posts
	 */
	public List<String> getPosts(){
		return posts;
	}
    
	/**
	 * Set the response list of posts
	 * 
	 * @param posts new list of posts
	 * @throws IllegalArgumentException if list is null or outside length range or the post is out of range
	 */
	public void setPosts(List<String> posts) throws IllegalArgumentException{
		if(posts == null || !isLegalPostsNumber(posts.size())){
			throw new IllegalArgumentException("Posts list size not legal");
		}
		
		for(String s : posts){
			if(s == null || !isLegalPost(s)){
				throw new IllegalArgumentException("Post length out of range");
			}
		}
		
		this.posts = posts;
	}
	
	/**
	 * Set the response error code
	 * 
	 * @param errorCode new error code
	 */
	public void setErrorCode(ErrorCode errorCode){
		this.errorCode = errorCode;
	}
	
	/**
	 * Set the response error value
	 * 
	 * @param errorCodeValue new error value
	 * @throws IllegalArgumentException if the error code value is out of range
	 */
	public void setErrorCode(int errorCodeValue) throws IllegalArgumentException{
		this.errorCode = ErrorCode.getErrorCode(errorCodeValue);
	}
	
	
	/**
     * Returns type of the packet
     * 
     * @return packet type
     */
	public String getType(){
		return TYPE;
	}
	
	/**
     * Get the query ID
     * 
     * @return current query ID
     */
    public long getQueryId(){
    	return queryId;
    }
    
    /**
     * Set the message query ID
     * 
     * @param queryId the new query ID
     * @throws IllegalArgumentException if the given ID is out of range
     */
    public void setQueryId(long queryId) throws IllegalArgumentException{
    	if(!isLegalId(queryId)){
    		throw new IllegalArgumentException("Illegal ID");
    	}
    	this.queryId = queryId;
    }
    
    /**
     * Get the message error code
     * 
     * @return message error code
     */
    public ErrorCode getErrorCode(){
    	return errorCode;
    }
    
	/**
     * Serialize the HoOn Response
     * 
     * @return serialized HoOn Response
     * @throws HoOnException if error during serialization
	 * @throws IOException if I/O exception
     */
	@Override
	public byte[] encode() throws HoOnException, IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		out.writeByte(HoOnMessage.RESPONSE_HEADER);
		
		int errorCodeValue = errorCode.getErrorCodeValue();
		byte errorCodeValueByte = (byte) errorCodeValue;
		out.writeByte(errorCodeValueByte);
		
		int tmp = (int) (queryId & 0xffffffffL);
		out.writeInt(tmp);
		
		short numberOfPost = (short) (posts.size() & 0xffff);
		out.writeShort(numberOfPost);
		
		for(String s : posts){
			short lengthOfPost = (short) (s.length() & 0xffff);
			out.writeShort(lengthOfPost);
			out.writeBytes(s);
		}
		
        out.flush();
		
		return b.toByteArray();
	}
    
	/**
	 * Returns the hashCode
	 * 
	 * @return Returns the hashCode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result + ((posts == null) ? 0 : posts.hashCode());
		result = prime * result + (int) (queryId ^ (queryId >>> 32));
		return result;
	}
    
	/**
	 * Compare if two HoOnResponse are the same
	 * 
	 * @return return true if they has the same contents; otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoOnResponse other = (HoOnResponse) obj;
		if (errorCode != other.errorCode)
			return false;
		if (posts == null) {
			if (other.posts != null)
				return false;
		} else if (!posts.equals(other.posts))
			return false;
		if (queryId != other.queryId)
			return false;
		return true;
	}
}

/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization;

import java.io.IOException;

/**
 * Represents a InstaYakError message and provides serialization/deserialization
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakError extends InstaYakMessage{
	
	/**
	 * the Error message
	 */
	private String error;
	
	/**
	 * constant operation
	 */
	public static final String OPERATION = "ERROR";
	
	/**
	 * check if a String can be a valid error message
	 * 
	 * @param s the String need to be checked
	 * @return true if a String can be a valid error message
	 * @throws InstaYakException if validation fails
	 */
	public static boolean isValidErrorMessage(String s) throws InstaYakException{
		if(s == null){
    		throw new InstaYakException("Error message is null");
    	}
    	
    	if(s.length() <= 0){
     		throw(new InstaYakException("Error message should be longer than 0"));
     	}
    	
     	for(int i = 0; i < s.length(); ++i){
     		if(s.charAt(i) != ' ' && !Character.isAlphabetic(s.charAt(i)) && !Character.isDigit(s.charAt(i))){
     			throw(new InstaYakException("Invalid characters in error message"));
     		}
     	}
    	
		return true;
	}
	
	/**
	 * Constructs Error message using set values
	 * 
	 * @param message error message
	 * 
	 * @throws InstaYakException if validation fails
	 */
	public InstaYakError(String message) throws InstaYakException{
		if(isValidErrorMessage(message)){
			  error = message;
		}
	}
	
	/**
	 * Constructs new InstaYakError message using deserialization. Only parses material specific to this message
	 *     (that is not operation)
	 * 
	 * @param in deserialization input source
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If it is not "Error" message
	 */
	public InstaYakError(MessageInput in) throws InstaYakException, IOException{
		InstaYakMessage msg = InstaYakMessage.decode(in);
		
		if(msg.getOperation() != OPERATION){
			throw new InstaYakException("Wrong operation");
		}
		
		error = ((InstaYakError)msg).getMessage();
	}
	
	/**
	 * Returns a String representation ("Error: Message=Bad stuff")
	 * 
	 * @return a String representation
	 */
    @Override
    public String toString(){
    	return "Error: Message=" + error;
    }
    
    /**
     * Returns message
     * 
     * @return return the error message
     */
    public final String getMessage(){
    	return error;
    }
    
    
    /**
     * Sets message
     * 
     * @param message new message
     * @throws InstaYakException if null or invalid message
     */
    public final void setMessage(String message) throws InstaYakException{
    	if(isValidErrorMessage(message)){
    		error = message;	
    	}
    }
    
    /**
     * Returns Error operation
     * 
     * @return Error operation
     */
    @Override
    public String getOperation(){
    	return OPERATION;
    }
    
	/**
	 * Serializes message to to given output sink
	 * 
	 * @param out serialization out put sink
	 * 
	 * @throws java.io.IOException if I/I problem
	 */
	@Override
	public void encode(MessageOutput out) throws java.io.IOException{
		String s = OPERATION + " " + error + InstaYakMessage.ENDING;
		byte[] encoding = s.getBytes(PROTOCOL);
		out.write(encoding);
	}

	/**
	 * hashCode for InstaYakError
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((error == null) ? 0 : error.hashCode());
		return result;
	}

	/**
	 * Override equals methods for InstaYakError class
	 * 
	 * @param obj the object needs to be compared
	 * 
	 * @return true/false if they are both InstaYakError objects; otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstaYakError other = (InstaYakError) obj;
		if (error == null) {
			if (other.error != null)
				return false;
		} else if (!error.equals(other.error))
			return false;
		return true;
	}	
}

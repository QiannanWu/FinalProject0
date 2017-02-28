/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization;

import java.io.IOException;

/**
 * Represents an InstaYakID and provides serialization/
 *     deserialization
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 *
 */
public class InstaYakID extends InstaYakMessage{
	
	/**
	 * the ID information
	 */
	private String id;
	
	/**
	 * constant operation
	 */
	public static final String OPERATION = "ID";
	
	
	/**
	 * check if a String can be a valid ID
	 * 
	 * @param s the String need to be checked
	 * @return true if a String can be a valid ID
	 * @throws InstaYakException if validation fails
	 */
	public static boolean isValidID(String s) throws InstaYakException{
		if(s == null){
     		throw(new InstaYakException("ID cannot be null"));
        }
		
    	if(s.length() <= 0){
    		throw(new InstaYakException("ID should be longer than 0"));
    	}
    	
    	for(int i = 0; i < s.length(); ++i){
    		if(!Character.isAlphabetic(s.charAt(i)) && !Character.isDigit(s.charAt(i))){
    			throw(new InstaYakException("Invalid characters in ID"));
    		}
    	}
    	
		return true;
	}
	
	/**
	 * Constructs ID message using set values
	 * 
	 * 
	 * @param ID ID for user
	 * 
	 * @throws InstaYakException if validation fails
	 */
    public InstaYakID(String ID) throws InstaYakException{
    	if(isValidID(ID)){
    		id = ID;
    	} 
    }
    
    /**
     * Constructs ID message using deserialization. Only parses material 
     * specific to this message
     * 
     * @param in deserialization input source
     * 
     * @throws InstaYakException if parse or validation failure
     * @throws java.io.IOException if I/O problem
     */
    public InstaYakID(MessageInput in) throws InstaYakException, IOException{
    	if(in == null){
    		throw(new NullPointerException("MessageInput cannot be null in ID constructor"));
    	}
    	
    	InstaYakMessage msg = InstaYakMessage.decode(in);
        if(msg.getOperation() != OPERATION){
        	throw(new InstaYakException("Wrong Operation"));
        }
        
    	id = ((InstaYakID)msg).getID();
    }
    
    /**
     * Returns a String representation ("ID:ID=bob")
     * 
     * @return return string representation
     */
    @Override
    public String toString(){
    	return "ID: ID=" + id;
    }
    
    /**
     * Returns ID
     * 
     * @return ID
     */
    public final String getID(){
    	return id;
    }
    
    /**
     * Sets ID
     * 
     * @param ID new ID
     * 
     * @throws InstaYakException if null or invalid ID
     */
     public final void setID(String ID) throws InstaYakException{
        if(isValidID(ID)){
        	id = ID;
        }	
     } 
     
     /**
      * Returns message operation
      * 
      * @return the message operation ("ID")
      */
     @Override
     public String getOperation(){
    	return OPERATION; 
     }
     
     /**
      * Serializes message to given output sink
      * 
      * @param out serialization output sink
      * 
      * @throws java.io.IOException if I/O problem
      */
     public void encode(MessageOutput out) throws IOException{
    	 String s = OPERATION + " " + id + InstaYakMessage.ENDING;
         byte[] encoding = s.getBytes(PROTOCOL);
         out.write(encoding);
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Compare if two InstaYakID are the same
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
		InstaYakID other = (InstaYakID) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
     
     
}

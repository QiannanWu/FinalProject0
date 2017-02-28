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
 * Represents a InstaYakChallenge and provides serialization/
 *     deserialization
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 *
 */
public class InstaYakChallenge extends InstaYakMessage{
	
	/**
	 * the Nonce information
	 */
	private String nonce;
	
	/**
	 * constant operation information
	 */
	public static final String OPERATION = "CLNG";
	
	
	/**
	 * check if a String can be a valid nonce
	 * 
	 * @param s the String need to be checked
	 * @return true if a String can be a valid nonce
	 * @throws InstaYakException if validation fails
	 */
	public static boolean isValidNonce(String s) throws InstaYakException{
		if(s == null){
     		throw(new InstaYakException("nonce cannot be null"));
        }
		
    	if(s.length() <= 0){
    		throw(new InstaYakException("Nonce should be longer than 0"));
    	}
    	
    	for(int i = 0; i < s.length(); ++i){
    		if(!Character.isDigit(s.charAt(i))){
    			throw(new InstaYakException("Invalid characters in Nonce"));
    		}
    	}
    	
		return true;
	}
	
	
	/**
	 * Constructs challenge message using given values.
	 * 
	 * @param nonce challenge nonce
	 * 
	 * @throws InstaYakException if validation fails
	 */
	public InstaYakChallenge(String nonce) throws InstaYakException{
		if(isValidNonce(nonce)){
			this.nonce = nonce;
		}
		
	}
	
	/**
	 * Constructs challenge message using deserialization. Only parses 
	 *     material specific to this message (that is not operation)
	 *     
	 * @param in deserialization input source
	 * 
	 * @throws InstaYakException if parse or validation failure
	 * @throws java.io.IOException if I/O problem
	 */
	public InstaYakChallenge(MessageInput in) throws InstaYakException, IOException{
		if(in == null){
     		throw(new InstaYakException("null nonce"));
        }
		
		InstaYakMessage msg = InstaYakMessage.decode(in);
		
		if(msg.getOperation() != OPERATION){
        	throw(new InstaYakException("Wrong Operation"));
        }
		
		nonce = ((InstaYakChallenge)msg).getNonce();
	} 
	
	/**
	 * Returns a String representation ("Challenge:Nonce=12345") 
	 * 
	 * @return string representation of nonce
	 */
	@Override
	public String toString(){
		return "Challenge: Nonce=" + nonce;
	}
	
	
	/**
	 * Returns nonce
	 * 
	 * @return nonce information
	 */
	public final String getNonce(){
		return nonce;
	}
	
	/**
	 * Sets nonce
	 * 
	 * @param nonce new nonce
	 * 
	 * @throws InstaYakException if null or invalid nonce
	 */
	public void setNonce(String nonce) throws InstaYakException{
		if(isValidNonce(nonce)){
			this.nonce = nonce;	
		}
	}
	
	/**
	 * Returns message operation
	 * 
	 * @return Returns the message operation
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
	@Override
	public void encode(MessageOutput out) throws java.io.IOException{
		String s = OPERATION + " " + nonce + InstaYakMessage.ENDING;
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
		result = prime * result + ((nonce == null) ? 0 : nonce.hashCode());
		return result;
	}

	/**
	 * Compare if two InstaYakChallenge are the same
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
		InstaYakChallenge other = (InstaYakChallenge) obj;
		if (nonce == null) {
			if (other.nonce != null)
				return false;
		} else if (!nonce.equals(other.nonce))
			return false;
		return true;
	}
	
	
}

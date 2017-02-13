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
 * Represents a InstaYakCredential message and provides serialization/deserialization
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakCredentials extends InstaYakMessage{
    
	/**
	 * the hash of the Credential
	 */
	private String hash;
	
	/**
	 * constant operation
	 */
	public static final String OPERATION = "CRED";
	
	/**
	 * Constructs credentials message using given hash
	 * 
	 * @param hash hash for credentials
	 * @throws InstaYakException if validation of hash fails
	 */
	public InstaYakCredentials(String hash) throws InstaYakException{
		if(isValidHash(hash)){
			this.hash = hash;
		}
	}
	
	/**
	 * Check if a String is a valid MD5 hash
	 * 
	 * @param hash the hash String needs to be check
	 * @return true if the hash is valid
	 * @throws InstaYakException if the hash is not valid
	 */
	public static boolean isValidHash(String hash) throws InstaYakException{
		if(hash == null){
			throw new InstaYakException("The hash is null");
		}
		
		if( hash.length() != 32 ){
			throw new InstaYakException("The hash has invalid length");
		}
		
		for(int i = 0; i < hash.length(); ++i){
			if((!Character.isUpperCase(hash.charAt(i)) && !Character.isDigit(hash.charAt(i))) || 
					(Character.isUpperCase(hash.charAt(i)) && hash.charAt(i) > 'F')){
				throw new InstaYakException("Invalid character in hash");
			}
		}
		
		return true;
	}
	
	
	/**
	 * Constructs credentials message using deserialization.
	 * 
	 * @param in deserialization input source
	 * @throws InstaYakException if parse or validation failure
	 * @throws IOException if I/O problem
	 */
	public InstaYakCredentials(MessageInput in) throws InstaYakException, IOException{
        InstaYakMessage msg = InstaYakMessage.decode(in);
		
		if(msg.getOperation() != OPERATION){
			throw new InstaYakException("Wrong operation");
		}
		
		hash = ((InstaYakCredentials)msg).getHash();
	}
	
	/**
	 * Returns s String representation ("Credentials: Hash=12345")
	 * 
	 * @return a String representation
	 */
	@Override
	public String toString(){
		return "Credentials: Hash=" + hash;
	}
	
	/**
	 * Returns hash
	 * 
	 * @return hash of the credential
	 */
	public final String getHash(){
		return hash;
	}
	
	/**
	 * Sets hash
	 * @param hash new hash
	 * @throws InstaYakException if null or invalid hash
	 */
	public final void setHash(String hash) throws InstaYakException{
		if(isValidHash(hash)){
			this.hash = hash;
		}
	}
	
	/**
	 * Returns message operation
	 * 
	 * @return message operation
	 */
	@Override
	public String getOperation() {
		return OPERATION;
	}
    
	/**
	 * Serializes message to given output sink
	 * 
	 * @param out serialization output sink
	 * 
	 * @throws IOException if I/O problem
	 */
	@Override
	public void encode(MessageOutput out) throws IOException {
		String s = OPERATION + " " + hash + "\r\n";
		byte[] encoding = s.getBytes(PROTOCOL);
		out.write(encoding);
	}

	/**
	 * hashCode for InstaYakCredentials
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		return result;
	}

	/**
	 * Override equals methods for InstaYakCredentials class
	 * 
	 * @param obj the object needs to be compared
	 * 
	 * @return true/false if they have the same hash; otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstaYakCredentials other = (InstaYakCredentials) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}
	
	
}

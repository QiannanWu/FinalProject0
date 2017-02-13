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
 * Represents a InstaYakVersion and provides serialization/
 * deserialization
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 *
 */
public class InstaYakVersion extends InstaYakMessage{
    
	/**
	 * the version information
	 */
	private String version;
	
	/**
	 * constant version information
	 */
	public static final String VERSION = "1.0";
	
	/**
	 * constant operation information
	 */
	public static final String OPERATION = "INSTAYAK";
	
	/**
	 * Constructs version message
	 */
	public InstaYakVersion(){
		version = VERSION;
	}
	
	/**
	 * Constructs version message using deserialization. 
	 *     Only parses material specific to this message.
	 *     
	 * @param in deserialiazation input source
	 * 
	 * @throws InstaYakException
	 *      if parse or validation failure
	 * 
	 * @throws java.io.IOException
	 *      if I/O problem
	 */
	public InstaYakVersion(MessageInput in) throws InstaYakException, IOException{
		if(in == null){
    		throw(new NullPointerException());
    	}
		InstaYakMessage msg = InstaYakMessage.decode(in);
		if(msg.getOperation() != OPERATION){
        	throw(new InstaYakException("Wrong Operation"));
        }
        version = ((InstaYakVersion)msg).getVersion();
	}
	
    /**
	 * Returns a String representation ("InstaYak")
	 * 
	 * @return a string representation
	 */
	@Override
	public String toString(){
		return "InstaYak";
	}
	
	/**
	 * Returns version Message
	 * 
	 * @return a string of version
	 */
	public String getVersion(){
		return version;
	}
	
	/**
	 * Returns message operation
	 * 
	 * @return the message operation ("INSTAYAK")
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
		String s = OPERATION + " " + version + "\r\n";
		byte[] encoding = s.getBytes(PROTOCOL);
		out.write(encoding);
	}
	
	/**
	 * hashCode for InstaYakVersion
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	/**
	 * Override equals methods for InstaYakVersion class
	 * 
	 * @param obj the object needs to be compared
	 * 
	 * @return true/false if they have the same version value, return true; otherwise
	 *             return false;
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstaYakVersion other = (InstaYakVersion) obj;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
	
}

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
 * Represents a InstaYakSLMD message and provides serialization/deserialization
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakSLMD extends InstaYakMessage{
	/**
	 * the SLMD message
	 */
	private String SLMD;
	
	/**
	 * constant operation
	 */
	public static final String OPERATION = "SLMD";
	
	/**
	 * Constructs SLMD message
	 */
	public InstaYakSLMD(){
		SLMD = OPERATION;
	}
	
	/**
	 * Constructs new SLMD message using deserialization. Only parses material specific to this message
	 *     (that is not operation)
	 * 
	 * @param in deserialization input source
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If it is not "SLMD" message
	 */
	public InstaYakSLMD(MessageInput in) throws InstaYakException, IOException{
		InstaYakMessage msg = InstaYakMessage.decode(in);
		
		if(msg.getOperation() != OPERATION){
			throw new InstaYakException("Wrong operation");
		}
		else{
			SLMD = msg.getOperation();
		}
	}
	
	/**
	 * Returns a String representation
	 * 
	 * @return a String representation
	 */
    @Override
    public String toString(){
    	return SLMD;
    }
    
    /**
     * Returns SLMD operation
     * 
     * @return SLMD operation
     */
    @Override
    public String getOperation(){
    	return SLMD;
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
		String s = SLMD + "\r\n";
		byte[] encoding = s.getBytes(PROTOCOL);
		out.write(encoding);
	}

	/**
	 * hashCode for InstaYakSLMD
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SLMD == null) ? 0 : SLMD.hashCode());
		return result;
	}

	/**
	 * Override equals methods for InstaYakSLMD class
	 * 
	 * @param obj the object needs to be compared
	 * 
	 * @return true/false if they are both InstaYakSLMD objects; otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstaYakSLMD other = (InstaYakSLMD) obj;
		if (SLMD == null) {
			if (other.SLMD != null)
				return false;
		} else if (!SLMD.equals(other.SLMD))
			return false;
		return true;
	}
}

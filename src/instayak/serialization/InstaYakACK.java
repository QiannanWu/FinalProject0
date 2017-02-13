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
 * Represents a InstaYakACK message and provides serialization/deserialization
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakACK extends InstaYakMessage {

	/**
	 * store the ACK message
	 */
	private String ACK;
    
	/**
	 * constant operation information
	 */
	public static final String OPERATION = "ACK";
	
	/**
	 * Constructs ACK message
	 */
	public InstaYakACK() {
		ACK = OPERATION;
	}

	/**
	 * Constructs new ACK message using deserialization. Only parses material
	 * specific to this message (that is not operation)
	 * 
	 * @param in deserialization input source
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If it is not "ACK" message
	 */
	public InstaYakACK(MessageInput in) throws InstaYakException, IOException {
		InstaYakMessage msg = InstaYakMessage.decode(in);

		if (msg.getOperation() != OPERATION) {
			throw new InstaYakException("Wrong operation");
		}
		
	    ACK = ((InstaYakACK) msg).getOperation();
	}

	/**
	 * Returns a String representation
	 * 
	 * @return a String representation
	 */
	@Override
	public String toString() {
		return ACK;
	}

	/**
	 * Returns ACK operation
	 * 
	 * @return ACK operation
	 */
	@Override
	public String getOperation() {
		return ACK;
	}

	/**
	 * Serializes message to to given output sink
	 * 
	 * @param out
	 *            serialization out put sink
	 * 
	 * @throws java.io.IOException
	 *             if I/I problem
	 */
	@Override
	public void encode(MessageOutput out) throws java.io.IOException {
		String s = ACK + "\r\n";
		byte[] encoding = s.getBytes(PROTOCOL);
		out.write(encoding);
	}

	/**
	 * hashCode for InstaYakACK
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ACK == null) ? 0 : ACK.hashCode());
		return result;
	}

	/**
	 * Override equals methods for InstaYakACK class
	 * 
	 * @param obj the object needs to be compared
	 * 
	 * @return true/false if they are both InstaYakACK objects; otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstaYakACK other = (InstaYakACK) obj;
		if (ACK == null) {
			if (other.ACK != null)
				return false;
		} else if (!ACK.equals(other.ACK))
			return false;
		return true;
	}
}

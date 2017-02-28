/************************************************
*
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization;

/**
 * Exception for InstaYak handling
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 */
public class InstaYakException extends Exception {
	/**
	 * serialVersionUID the default serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs InstaYak exception
     * @param message exception message
     * @param cause exception cause
	 * @throws AssertionError If the message is null
     */
	public InstaYakException(String message, Throwable cause){
		super(message, cause);
    }
    
	 /**
     * Constructs InstaYak exception
     * @param message exception message
     */
    public InstaYakException(String message){
    	super(message);
    }
}

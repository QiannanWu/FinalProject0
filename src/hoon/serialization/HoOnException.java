/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization;
import java.io.Serializable;

/**
 * Exception class used for signaling failure of HoOn message creation/management
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public class HoOnException extends Exception implements Serializable{
	
	/**
	 * The default serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The error code for this exception
	 */
	private ErrorCode errorCode;

	/**
	 * Construct a HoOnException with the given error code
	 * 
	 * @param errorCode the error code
	 */
    public HoOnException(ErrorCode errorCode){
    	super(errorCode.getErrorMessage());
    	this.errorCode = errorCode;
    }
    
    /**
     * Construct a HoOnException with the given error code and cause
     * @param errorCode the error code
     * @param cause the cause for error
     */
    public HoOnException(ErrorCode errorCode, Throwable cause){
    	super(errorCode.getErrorMessage(), cause);
    	this.errorCode = errorCode;
    }
    
    /**
     * Get the error code for the exception
     * @return the error code
     */
    public ErrorCode getErrorCode(){
    	return errorCode;
    }
}

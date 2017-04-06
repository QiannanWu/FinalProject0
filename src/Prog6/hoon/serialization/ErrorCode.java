/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization;

/**
 * Allowable error codes with associated values and error messages
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public enum ErrorCode {
	/**
	 * NOERROR It indicates no error
	 * BADVERSION It indicates a message with a bad version was received
	 * UNEXPECTEDERRORCODE It indicates a message with an unexpected error code was received
	 * UNEXPECTEDPACKETTYPE It indicates a message with a unexpected packet type was received
	 * PACKETTOOLONG It indicates a message with extraneous trailing bytes was received
	 * PACKETTOOSHORT It indicates a message with insufficient bytes was received
	 * NETWORKERROR It indicates some network error occurred
	 */
    NOERROR(0, "No error"), BADVERSION(1, "Bad version"), 
    UNEXPECTEDERRORCODE(2, "Unexpected error code"), UNEXPECTEDPACKETTYPE(3, "Unexpected packet type"), 
    PACKETTOOLONG(4, "Packet too long"), PACKETTOOSHORT(5, "Packet too short"),
    NETWORKERROR(7, "Network error");
	private int value; // the error code value
	private String message; // the error code message
	
	/**
	 * Initialize value of ErrorCode
	 * @param value the value of the error
	 * @param message the error message
	 */
	ErrorCode(int value, String message){
		this.value = value;
		this.message = message;
	}
	
	/**
	 * Get the error value(0-6)
	 * 
	 * @return the value associated with the error code
	 */
	public int getErrorCodeValue(){
		return value; 
	}
	
	/**
	 * Get the error message
	 * 
	 * @return the message associate with the error code
	 */
	public String getErrorMessage(){
		return message;
	}
	
	/**
	 * Get the error code associated with the given error value
	 * 
	 * @param errorCodeValue error value
	 * @return error code associated with given value
	 * @throws IllegalArgumentException if error value is out of range
	 */
	public static ErrorCode getErrorCode(int errorCodeValue) throws IllegalArgumentException{
		for(ErrorCode e : ErrorCode.values()){
			if(e.getErrorCodeValue() == errorCodeValue){
				return e;
			}
		}	
		throw new IllegalArgumentException();
	}
}

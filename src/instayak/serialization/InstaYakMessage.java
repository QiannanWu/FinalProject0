/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization;

import java.io.IOException;
import java.util.Base64;

/**
 * Represents generic portion of InstaYak message and provides serialization/
 *     deserialization
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 * 
 */
public abstract class InstaYakMessage {
	/**
	 * constant protocol information
	 */
	public static final String PROTOCOL = "ISO8859-1";
	
	/**
	 * const space character 
	 */
	public static final char SPACE = ' ';
	
	/**
	 * const ending characters 
	 */
	public static final String ENDING = "\r\n";
	
	/**
	 * one const ending character
	 */
	public static final char VALID_CHAR = '\r';
	
	/**
	 * Parse a string to two parts, seperated by a space
	 * 
	 * @param original the string needs to be parsed.
	 * 
	 * @return the a String array contains 2 Strings
	 */
	public static String[] split(String original){
		String[] s = new String[2];
		int length = 0;
		String first = "";
		while(length < original.length() && original.charAt(length) != SPACE){
			first += original.charAt(length);
			length ++;
		}
		
		String second = original.substring(length, original.length());
		
		s[0] = first;
		s[1] = second;
		return s;
	}
	
	/**
	 * Deserializes message from input source
	 * 
	 * @param in deserialization input source
	 * 
	 * @return a specific InstaYak message resulting from deserialization
	 * 
	 * @throws InstaYakException if parse or validation problem
	 * @throws java.io.IOException if I/O problem
	 */
    public static InstaYakMessage decode(MessageInput in) throws InstaYakException, IOException{
    	String original = in.getOneMessage();
    	
       	if(original == null){
    		throw(new InstaYakException("Enpty MessageInput??"));
    	}
    	
    	if(original.charAt(original.length() - 1) != VALID_CHAR){
			throw(new InstaYakException("Invalid Ending"));
		}
        original = original.substring(0, original.length() - 1);    	
    	String[] s = split(original);
    	String sub;
    	if(s[1].length() >= 1){
    		sub = s[1].substring(1, s[1].length());
    	}
    	else{
    		sub = "";
    	}
    	 
        if(s[0] == null){
        	throw new NullPointerException("Error in Parsing");
        }
        
        switch(s[0]){
        	case InstaYakVersion.OPERATION:
        		if(!InstaYakVersion.VERSION.equals(sub) || s[1].length()< 1 || s[1].charAt(0) != SPACE){
        			throw(new InstaYakException("Version validation fail"));
        		}
        		return new InstaYakVersion();
        	case InstaYakID.OPERATION:
        		if(s[1].length()< 1 || s[1].charAt(0) != SPACE || !InstaYakID.isValidID(sub)){
    			    throw(new InstaYakException("ID validation fail"));
    		    }
        		
    		    return new InstaYakID(sub);
        	case InstaYakChallenge.OPERATION:
        		if(s[1].length()< 1 || s[1].charAt(0) != SPACE || !InstaYakChallenge.isValidNonce(sub)){
        			throw(new InstaYakException("CLNG validation fail"));
        		}
        		
        		return new InstaYakChallenge(sub);
        	case InstaYakSLMD.OPERATION:
        		if(sub != ""){
        			throw(new InstaYakException("SLMD validation fail"));
        		}
        		return new InstaYakSLMD();
        		
        	case InstaYakACK.OPERATION:
        		if(sub != ""){
        			throw(new InstaYakException("ACK validation fail"));
        		}
                
        		return new InstaYakACK();
        	case InstaYakError.OPERATION:
        		if(s[1].length()< 1 || s[1].charAt(0) != SPACE || !InstaYakError.isValidErrorMessage(sub)){
        			throw(new InstaYakException("Error validation fail"));
        		}
        		
        		return new InstaYakError(sub);
        	case InstaYakCredentials.OPERATION:
        		if(s[1].length()< 1 || s[1].charAt(0) != SPACE || !InstaYakCredentials.isValidHash(sub)){
        			throw new InstaYakException("Hash string validation fail");
        		}
        		
        		return new InstaYakCredentials(sub);
        	case InstaYakUOn.OPERATION:
        		if(s[1].length()< 1 || s[1].charAt(0) != SPACE){
        			throw(new InstaYakException("UOn validation fail"));
        		}
        		
        		String[] uon = split(sub);
        		if(uon[0].length() < 1 || !InstaYakUOn.isValidCategory(uon[0])){
        			throw new InstaYakException("category string validation fail");
        		}
        		
        		if(uon[1].length()<= 1 || uon[1].charAt(0) != SPACE){
        			throw(new InstaYakException("Image validation fail"));
        		}
        		
        		String imgString;
        		imgString = uon[1].substring(1, uon[1].length());
            	byte[] image = Base64.getDecoder().decode(imgString);
        		return new InstaYakUOn(uon[0], image);
        		
        	default:
        		throw(new InstaYakException("Unknown Operation"));
        }
    }
    
    /**
     * Returns message operation
     * 
     * @return message operation
     */
    public abstract String getOperation();
    /**
     * Serializes message to given output sink. It does nothing here because children will implement this method
     *     and serializes specific message to given output sink.
     * 
     * @param out serialization output sink
     * 
     * @throws java.io.IOException if I/O problem
     */
    public abstract void encode(MessageOutput out) throws java.io.IOException;
}

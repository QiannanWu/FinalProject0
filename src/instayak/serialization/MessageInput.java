/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Deserialization input source
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 */
public class MessageInput {
    /**
     * the byte array buffer for InputStream
     */
	private byte[] buffer;
	
	/**
	 * the length of the byte array buffer
	 */
	private int length;
	
	/**
	 * the place currently waiting for being read
	 */
	private int offset;
	
	/**
	 * Deserialization input source
	 * 
	 * @param in byte input source
	 * 
	 * @throws java.lang.NullPointerException If in is null
	 * @throws IOException If I/O problem
	 */
    public MessageInput(InputStream in) throws NullPointerException, IOException{
    	int estimate = in.available();
    	buffer = new byte[estimate];
    	length = in.read(buffer);
    	offset = 0;
    }
    
    
    /**
     * Get One Message from the input source
     * 
     * @return Returns the first line of the message in String format and separated the line by spaces
     * @throws InstaYakException If the string contains multiple spaces
     * @throws UnsupportedEncodingException if unsupported encoding error
     */
    public String getOneMessage() throws InstaYakException, UnsupportedEncodingException{
    	if(length == offset){ //If there is nothing to read, return null
    		return null;
    	}
    	int k = offset; // k is a local variable which keep track of the current offset
    	
    	while(k < length){
    		if(buffer[k] == '\r'){ //If we find a '\r', we need to find the next '\n'
    			if(k + 1 < length && buffer[k+1] == '\n'){
    			    break;
    			}
    			else{
    				throw new InstaYakException("Format Error: Finding \r but there is no \n following");
    			}
    		}
    		
    		k++;
    	}
    	if(k + 1 >= length || buffer[k] != '\r' || buffer[k+1] != '\n'){
    		throw new InstaYakException("Format Error: Bad Framing");
    	}
    	
    	
    	byte[] b = Arrays.copyOfRange(buffer, offset, k); // total length of the message without "\r\n"
    	
    	offset = k + 2;
    	String out = new String(b, InstaYakMessage.PROTOCOL);
        
    	return out;
    }
}

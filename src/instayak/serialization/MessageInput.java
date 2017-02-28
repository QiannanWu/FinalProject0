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
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Deserialization input source
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 */
public class MessageInput {
    
	/**
	 * The Scanner for the InputStream
	 */
	private Scanner sin;
	
	/**
	 * Deserialization input source
	 * 
	 * @param in byte input source
	 * 
	 * @throws java.lang.NullPointerException If in is null
	 * @throws IOException If I/O problem
	 */
	
    public MessageInput(InputStream in) throws NullPointerException, IOException{
    	sin = new Scanner(in, InstaYakMessage.PROTOCOL);
        sin.useDelimiter(Pattern.compile("\n"));
    }
    
    
    /**
     * Get One Message from the input source
     * 
     * @return Returns the first line of the message in String format and separated the line by spaces
     */
    public String getOneMessage(){
    	if(sin.hasNext(Pattern.compile(".*\r.*"))){
    		String out = sin.next(Pattern.compile(".*\r"));
    		
    		return out;
    	}
    	else{
    		return null;
    	}
    	   
    }
    
    /**
     * If has input, return true. Otherwise false; 
     * 
     * @return returns true if it has the next input
     */
    public boolean hasNext(){
    	return sin.hasNext();
    }
}

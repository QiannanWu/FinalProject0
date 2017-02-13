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
 * @param sin The Scanner for the InputStream
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 */
public class MessageInput {

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
     * @throws InstaYakException If the string contains multiple spaces
     */
    public String getOneMessage() throws IOException, InstaYakException{
    	//System.out.println(sin.hasNext(Pattern.compile(".*\r.*")));
    	if(sin.hasNext(Pattern.compile(".*\r.*"))){
    		String out = sin.next(Pattern.compile(".*\r"));
    		
    		if(out.charAt(out.length() - 1) != '\r'){
    			throw(new InstaYakException("Invalid"));
    		}
    		
    		return out.substring(0, out.length() - 1);
    	}
    	else{
    		return null;
    	}
    	   
    }
}

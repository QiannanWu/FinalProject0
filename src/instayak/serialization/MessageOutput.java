/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Serialization output sink
 *
 * @version 1.0 19 January 2017
 * @author Qiannan Wu
 */
public class MessageOutput {
	/**
	 * The OutputStream
	 */
	OutputStream out;
	
	/**
	 * Constructs a new output sink from an OutputStream
	 * 
	 * @param out byte output sink
	 * @throws NullPointerException if OutputStream is null
	 */
    public MessageOutput(OutputStream out) throws NullPointerException{
    	if(out == null){
    		throw(new NullPointerException());
    	}
    	
        this.out = out;
    }
    
    /**
     * Write to the output stream
     * 
     * @param encoding the encoded bytes
     * 
     * @throws IOException If I/O problems
     * @throws NullPointerException If out is null
     */
    public void write(byte[] encoding) throws IOException, NullPointerException{
    	out.write(encoding);
    }
}

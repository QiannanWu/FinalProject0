/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakVersion;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakVersion class
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu, sneha
 */
public class InstaYakVersionTest {
    /**
     * the byte[] array for encoding test
     */
	private static final byte[] encoding;
	
	/**
	 * the byte[] array for encoding test with multiple input lines
	 */
	private static final byte[] encodingMultiLines;
	
	/**
	 * the byte[] array for encoding test with other operation 
	 */
	private static final byte[] encodingWrongOperation;
	
	/**
	 * the byte[] array for encoding test invalid version
	 */
	private static final byte[] encodingValidationError;
	
	/**
	 * InputStream for encoding test
	 */
	private static InputStream in;
	
	/**
	 * the InputStream for encoding test with multiple input lines
	 */
	private static InputStream inMultiLines;
	
	/**
	 * the InputStream for encoding test with other operation 
	 */
	private static InputStream inWrongOperation;
	
	/**
	 * the InputStream for encoding test invalid version
	 */
	private static InputStream inValidationError;
	
	static{
		try{
			encoding = "INSTAYAK 1.0\r\n".getBytes("ISO8859-1");
			encodingMultiLines = "INSTAYAK 1.0\r\nINSTAYAK 1.0\r\nINSTAYAK 1.0\r\n".getBytes("ISO8859-1");
			encodingWrongOperation = "ID 123\r\n".getBytes("ISO8859-1");
			encodingValidationError = "INSTAYAK 2.0\r\n".getBytes("ISO8859-1");
		}catch(UnsupportedEncodingException e){
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	static{
		in = new ByteArrayInputStream(encoding);
		inMultiLines = new ByteArrayInputStream(encodingMultiLines);
		inWrongOperation = new ByteArrayInputStream(encodingWrongOperation);
		inValidationError = new ByteArrayInputStream(encodingValidationError);
	}
	
    /**
     * Test default constructor
     */
    @Test
    public void testDefaultConstructor(){
        InstaYakVersion test = new InstaYakVersion();
        assertEquals(test.toString(), "InstaYak");
    }
    
    /**
     * Test constructor with a MessageInput parameter in InstaYakVersion
     * 
     * @throws IOException If I/O problems
     * @throws InstaYakException If unable to parse or validation failure
     */
    @Test
    public void testInstaYakVersionMessageInput() throws NullPointerException, InstaYakException, IOException{
    	InstaYakVersion test = new InstaYakVersion(new MessageInput(in));
		assertEquals(test.getVersion(), "1.0");
    }
	
    /**
	 * Test for constructor with a MessageInput parameter in InstaYakVersion
	 * 
     * @throws IOException If I/O problems
     * @throws InstaYakException If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakVersionMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakVersion test1 = new InstaYakVersion(in);
		assertEquals(test1.getVersion(), "1.0");
		InstaYakVersion test2 = new InstaYakVersion(in);
		assertEquals(test2.getVersion(), "1.0");
		InstaYakVersion test3 = new InstaYakVersion(in);
		assertEquals(test3.getVersion(), "1.0");
	}
    
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakVersion
	 *     Wrong Operation Exception
	 *     
     * @throws IOException If I/O problems
     * @throws InstaYakException If unable to parse or validation failure
	 */
    @Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException{
    	new InstaYakVersion(new MessageInput(inWrongOperation));
	}
    
    /**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakVersion
	 *     Validation Error Exception
	 *     
     * @throws IOException If I/O problems
     * @throws InstaYakException If unable to parse or validation failure
	 */
    @Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws InstaYakException, IOException{
    	new InstaYakVersion(new MessageInput(inValidationError));
	}
    
    /**
     * Test toString in InstaYakVersion 
     */
	@Test
	public void testToString() {
		InstaYakVersion test = new InstaYakVersion();
		assertEquals(test.toString(), "InstaYak");
	}
	
    /**
	 * Test getOperation in InstaYakVersion
	 */
	@Test
	public void testGetOperation() {
		InstaYakVersion test = new InstaYakVersion();
		assertEquals(test.getOperation(), "INSTAYAK");
	}
    
	/**
	 * Test encode in InstaYakVersion
	 * 
	 * @throws IOException If I/O problems
	 */
	@Test
	public void testEncode() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MessageOutput mout = new MessageOutput(bout);
		new InstaYakVersion().encode(mout);
		new InstaYakVersion().encode(mout);
		new InstaYakVersion().encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}
	
	/**
	 * Test encoding throwing exception in InstaYakVersion
	 * 
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If unable to parse or validation failure
	 */
	@Test(expected = NullPointerException.class)
	public void testEncodeException()throws IOException, InstaYakException{
		MessageOutput mout = new MessageOutput(null);
		new InstaYakVersion().encode(mout);
	}
	
	/**
	 * Test equals inInstaYakVersion
	 */
	@Test
	public void testEquals(){
		InstaYakVersion test1 = new InstaYakVersion();
		InstaYakVersion test2 = new InstaYakVersion();
		assertTrue(test1.equals(test2));
	}
}

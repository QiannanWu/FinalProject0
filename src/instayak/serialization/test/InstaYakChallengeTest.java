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

/**
 * Test for InstaChallenge class
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu, sneha
 */
import instayak.serialization.InstaYakChallenge;
import instayak.serialization.InstaYakException;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

public class InstaYakChallengeTest {
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
	 * the byte[] array for encoding test invalid version
	 */
	private static final byte[] encodingValidationError2;
	
	/**
	 * the byte[] array for encoding test invalid version
	 */
	private static final byte[] encodingValidationError3;
	
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
	
	/**
	 * the InputStream for encoding test invalid version
	 */
	private static InputStream inValidationError2;
	
	/**
	 * the InputStream for encoding test invalid version
	 */
	private static InputStream inValidationError3;
	
	static{
		try{
			encoding = "CLNG 1234\r\n".getBytes("ISO8859-1");
			encodingMultiLines = "CLNG 1234\r\nCLNG 2345\r\nCLNG 3456\r\n".getBytes("ISO8859-1");
			encodingWrongOperation = "ID bob".getBytes("ISO8859-1");
			encodingValidationError = "CLNG bob1\r\n".getBytes("ISO8859-1");
			encodingValidationError2 = "CLNG  bob2\r\n".getBytes("ISO8859-1");
			encodingValidationError3 = "".getBytes("ISO8859-1");
		}catch(UnsupportedEncodingException e){
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	static{
		in = new ByteArrayInputStream(encoding);
		inMultiLines = new ByteArrayInputStream(encodingMultiLines);
		inWrongOperation = new ByteArrayInputStream(encodingWrongOperation);
		inValidationError = new ByteArrayInputStream(encodingValidationError);
		inValidationError2 = new ByteArrayInputStream(encodingValidationError2);
		inValidationError3 = new ByteArrayInputStream(encodingValidationError3);
	}
	
	/**
	 * Test for constructor with a String parameter in InstaYakChallenge
	 * 
	 * @throws InstaYakException If validation or parsing error
	 */
	public void testStringConstructor() throws InstaYakException{
		InstaYakChallenge test = new InstaYakChallenge("1234");
	    assertEquals(test.getNonce(), "1234");
	}
	
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakChallenge
	 *     Nonce length less than 1 exception
	 *     
	 * @throws InstaYakException If validation or parsing error
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorLengthException() throws InstaYakException{
		new InstaYakChallenge("");
	}
    
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakChallenge
	 *     Nonce validation failure exception
	 *     
	 * @throws InstaYakException If validation or parsing error
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorValidationException() throws InstaYakException{
		new InstaYakChallenge("123a");
	}
	
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakChallenge
	 *     Nonce validation failure exception
	 *     
	 * @throws InstaYakException If validation or parsing error
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorValidationException2() throws InstaYakException{
		new InstaYakChallenge("1 23");
	}
	
	/**
	 * Test for constructor with a MessageInput parameter in InstaYakChallenge
	 * 
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If validation or parsing error
	 */
	@Test
	public void testInstaYakIDMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakChallenge test1 = new InstaYakChallenge(in);
		assertEquals(test1.getNonce(), "1234");
		InstaYakChallenge test2 = new InstaYakChallenge(in);
		assertEquals(test2.getNonce(), "2345");
		InstaYakChallenge test3 = new InstaYakChallenge(in);
		assertEquals(test3.getNonce(), "3456");
	}
	
	/**
	 * Test for constructor with a MessageInput parameter in InstaYakChallenge
	 * 
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If validation fails
	 */
	@Test
	public void testInstaYakIDMessageInput() throws InstaYakException, IOException {
		InstaYakChallenge test = new InstaYakChallenge(new MessageInput(in));
		assertEquals(test.getNonce(), "1234");
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakChallenge
	 *     Wrong Operation Exception
	 *     
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If validation fails
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException{
		new InstaYakChallenge(new MessageInput(inWrongOperation));
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakChallenge
	 *     Validation Exception
	 *     
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If validation fails
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws InstaYakException, IOException{
		new InstaYakChallenge(new MessageInput(inValidationError));
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakChallenge
	 *     Validation Exception
	 * 
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If validation fails
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError2() throws InstaYakException, IOException{
		new InstaYakChallenge(new MessageInput(inValidationError2));
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakChallenge
	 *     Validation Exception
	 *
	 * @throws IOException If I/O problems
	 * @throws InstaYakException If validation fails
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError3() throws InstaYakException, IOException{
		new InstaYakChallenge(new MessageInput(inValidationError3));
	}
	
    /**
     * Test	toString in InstaYakChallenge()
     * 
     * @throws InstaYakException If validation or parsing fails
     */
	@Test
	public void testToString() throws InstaYakException {
		InstaYakChallenge test = new InstaYakChallenge("1234");
	    assertEquals(test.toString(), "Challenge: Nonce=1234");
	}
	
	/**
	 * Test getNonce in InstaYakException
     * 
     * @throws InstaYakException If validation or parsing fails
	 */
	@Test
	public void testGetNonce() throws InstaYakException {
		InstaYakChallenge test = new InstaYakChallenge("1234");
	    assertEquals(test.getNonce(), "1234");
	}
	
	/**
	 * Test setNonce in InstaYakException
	 * 
	 * @throws InstaYakException If validation or parsing fails
	 */
	@Test
	public void testSetNonce() throws InstaYakException {
		InstaYakChallenge test = new InstaYakChallenge("1234");
	    test.setNonce("5678");
		assertEquals(test.getNonce(), "5678");
	}
	
	/**
	 * Test for setNonce throwing exception in InstaYakChallenge
	 *     Length Exception
	 *     
	 * @throws InstaYakException If validation or parsing fails
	 */
	@Test(expected = InstaYakException.class)
	public void testSetNonceLength() throws InstaYakException {
		InstaYakChallenge test = new InstaYakChallenge("1234");
		test.setNonce("");
	}
	
	/**
	 * Test for setNonce throwing exception in InstaYakChallenge
	 *     Validation Exception
	 *     
	 * @throws InstaYakException If validation or parsing fails
	 */
	@Test(expected = InstaYakException.class)
	public void testSetNonceValidationException() throws InstaYakException {
		InstaYakChallenge test = new InstaYakChallenge("1234");
		test.setNonce("1a23");
	}
	
	/**
	 * Test for setNonce throwing InstaYak exception in InstaYakChallenge
	 *     Validation Exception
	 * @throws InstaYakException If validation or parsing fails
	 */
	@Test(expected = InstaYakException.class)
	public void testSetNonceValidationException2() throws InstaYakException{
		InstaYakChallenge test = new InstaYakChallenge("1234");
		test.setNonce("1 2");
	}
	
	/**
	 * Test for getOperation in InstaYakChallenge
	 * 
	 * @throws InstaYakException If validation or parsing fails
	 */
	@Test
	public void testGetOperation() throws InstaYakException {
		InstaYakChallenge test = new InstaYakChallenge("1234");
		assertEquals(test.getOperation(), "CLNG");
	}
	
	
	/**
     * Test for encode in InstaYakChallenge
     * 
     * @throws IOException if I/O problem
     * @throws InstaYakException if validation failure
     */
	@Test
	public void testEncode()throws IOException, InstaYakException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MessageOutput mout = new MessageOutput(bout);
		new InstaYakChallenge("1234").encode(mout);
		new InstaYakChallenge("2345").encode(mout);
		new InstaYakChallenge("3456").encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}
	
	/**
	 * Test for encode throwing exception in InstaYakChallenge
	 * 
     * @throws IOException if I/O problem
     * @throws InstaYakException if validation failure
     */
	@Test(expected = NullPointerException.class)
	public void testEncodeException()throws IOException, InstaYakException{
		MessageOutput mout = new MessageOutput(null);
		new InstaYakChallenge("1234").encode(mout);
	}
	
	/**
     * Test equals in InstaYakChallenge
     * 
     * @throws IOException if I/O problem
     * @throws InstaYakException if validation failure
     */
	@Test
	public void testEquals()throws IOException, InstaYakException{
		InstaYakChallenge test1 = new InstaYakChallenge("1234");
		InstaYakChallenge test2 = new InstaYakChallenge("1234");
		assertTrue(test1.equals(test2));
	}
}

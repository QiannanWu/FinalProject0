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
import instayak.serialization.InstaYakID;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakID class
 * 
 * @version 1.0 19 January 2017
 * @author Qiannan Wu, sneha
 */
public class InstaYakIDTest {
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
			encoding = "ID bob\r\n".getBytes("ISO8859-1");
			encodingMultiLines = "ID ahh\r\nID qn\r\nID test\r\n".getBytes("ISO8859-1");
			encodingWrongOperation = "CLNG 1234".getBytes("ISO8859-1");
			encodingValidationError = "ID bob*1\r\n".getBytes("ISO8859-1");
			encodingValidationError2 = "ID bob 1\r\n".getBytes("ISO8859-1");
			encodingValidationError3 = "".getBytes("ISO8859-1");
			/*
			String decoding = new String(encoding, "ISO8859-1");
			System.out.println(decoding);*/
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
	 * Test for constructor with a String parameter in InstaYakID
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testStringConstructor() throws InstaYakException{
		InstaYakID test = new InstaYakID("bob");
		assertEquals(test.getID(), "bob");
	}
	
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakID
	 *     ID length less than 1 exception
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorLengthException() throws InstaYakException{
		new InstaYakID("");
	}
	
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakID
	 *     ID validation failure exception
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorValidationException() throws InstaYakException{
		new InstaYakID("1*2");
	}
	
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakID
	 *     ID validation failure exception
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorValidationException2() throws InstaYakException{
		new InstaYakID("1 a");
	}
	
	/**
	 * Test for constructor with a String parameter throwing exception in InstaYakID
	 *     ID validation failure exception
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorValidationException3() throws InstaYakException{
		new InstaYakID("12 ");
	}
	
	/**
	 * Test for constructor with a MessageInput parameter in InstaYakID 
	 * 
	 * @throws IOException if I/O problem
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testInstaYakIDMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakID test1 = new InstaYakID(in);
		assertEquals(test1.getID(), "ahh");
		InstaYakID test2 = new InstaYakID(in);
		assertEquals(test2.getID(), "qn");
		InstaYakID test3 = new InstaYakID(in);
		assertEquals(test3.getID(), "test");
	}
	
	
	/**
	 * Test for constructor with a MessageInput parameter in InstaYakID 
     *
	 * @throws IOException if I/O problem
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testInstaYakIDMessageInput() throws NullPointerException, InstaYakException, IOException {
		InstaYakID test = new InstaYakID(new MessageInput(in));
		assertEquals(test.getID(), "bob");
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakID\
	 *     Wrong Operation Exception
	 *     
	 * @throws IOException if I/O problem
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException{
		new InstaYakID(new MessageInput(inWrongOperation));
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakID\
	 *     Validation Exception
	 *     
	 * @throws IOException if I/O problem
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws NullPointerException, InstaYakException, IOException{
		new InstaYakID(new MessageInput(inValidationError));
	}
     
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakID\
	 *     Validation Exception
	 *     
	 * @throws IOException if I/O problem
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError2() throws NullPointerException, InstaYakException, IOException{
		new InstaYakID(new MessageInput(inValidationError2));
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in InstaYakID\
	 *     Validation Exception
	 *     
	 * @throws IOException if I/O problem
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError3() throws NullPointerException, InstaYakException, IOException{
		new InstaYakID(new MessageInput(inValidationError3));
	}
	
	/**
	 * Test for toString() in InstaYakID
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testToString() throws InstaYakException {
		InstaYakID test = new InstaYakID("test");
		assertEquals(test.toString(), "ID: ID=test");
	}
	
	/**
	 * Test for getID in InstaYakID
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testGetID() throws InstaYakException {
		InstaYakID test = new InstaYakID("test");
		assertEquals(test.getID(), "test");
	}
    
	/**
	 * Test for setID in InstaYakID
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testSetID() throws InstaYakException {
		InstaYakID test = new InstaYakID("test");
		test.setID("testSet");
		assertEquals(test.getID(), "testSet");
	}
	
	/**
	 * Test for setID throwing exception in InstaYakID
	 *     Length Exception
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetIDLength() throws InstaYakException {
		InstaYakID test = new InstaYakID("test");
		test.setID("");
	}
	
	/**
	 * Test for setID throwing exception in InstaYakID
	 *     Validation Exception
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetIDValidationException() throws InstaYakException {
		InstaYakID test = new InstaYakID("test");
		test.setID("1*a");
	}
	
	/**
	 * Test for setID throwing InstaYak exception in InstaYakID
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetIDValidationException2() throws InstaYakException{
		InstaYakID test = new InstaYakID("test");
		test.setID("1 a");
	}
	
	/**
	 * Test for getOperation in InstaYakID
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test
	public void testGetOperation() throws InstaYakException {
		InstaYakID test = new InstaYakID("test");
		assertEquals(test.getOperation(), "ID");
	}
		
    /**
     * Test for encode in InstaYakID
     * 
     * @throws IOException if I/O problem
     * @throws InstaYakException if validation failure
     */
	@Test
	public void testEncode()throws IOException, InstaYakException{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MessageOutput mout = new MessageOutput(bout);
		new InstaYakID("ahh").encode(mout);
		new InstaYakID("qn").encode(mout);
		new InstaYakID("test").encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}
	
	/**
     * Test for encode throwing exception in InstaYakID
     * 
     * @throws IOException if I/O problem
     * @throws InstaYakException if validation failure
     */
	@Test(expected = NullPointerException.class)
	public void testEncodeException()throws IOException, InstaYakException{
		MessageOutput mout = new MessageOutput(null);
		new InstaYakID("ahh").encode(mout);
	}
	
	/**
     * Test equals in InstaYakID
     * 
     * @throws IOException if I/O problem
     * @throws InstaYakException if validation failure
     */
	@Test
	public void testEquals()throws IOException, InstaYakException{
		InstaYakID test1 = new InstaYakID("test");
		InstaYakID test2 = new InstaYakID("test");
		assertTrue(test1.equals(test2));
	}
}

/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
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

import org.junit.Before;
import org.junit.Test;

import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakError;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakError class
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakErrorTest {

	/**
	 * the byte[] array for encoding test
	 */
	private byte[] encoding;

	/**
	 * the byte[] array for encoding test with multiple input lines
	 */
	private byte[] encodingMultiLines;

	/**
	 * the byte[] array for encoding test with other operation
	 */
	private byte[] encodingWrongOperation;

	/**
	 * InputStream for encoding test
	 */
	private InputStream in;

	/**
	 * the InputStream for encoding test with multiple input lines
	 */
	private InputStream inMultiLines;

	/**
	 * the InputStream for encoding test with other operation
	 */
	private InputStream inWrongOperation;

	/**
	 * Initialize test data
	 */
	@Before
	public void prepare() {
		try {
			encoding = "ERROR Somthing wrong\r\n".getBytes("ISO8859-1");
			encodingMultiLines = "ERROR Somthing wrong\r\nERROR Somthing wrong2\r\nERROR Somthing wrong 3\r\n"
					.getBytes("ISO8859-1");
			encodingWrongOperation = "SLDM\r\n".getBytes("ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
		in = new ByteArrayInputStream(encoding);
		inMultiLines = new ByteArrayInputStream(encodingMultiLines);
		inWrongOperation = new ByteArrayInputStream(encodingWrongOperation);
	}

	/**
	 * Test constructor with a String parameter
	 * 
	 * @throws InstaYakException
	 *             If validation error.
	 */
	@Test
	public void testDefaultConstructor() throws InstaYakException {
		InstaYakError test = new InstaYakError("Bad stuff");
		assertEquals(test.getMessage(), "Bad stuff");
	}

	/**
	 * Test constructor with a MessageInput parameter in InstaYakError
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakErrorMessageInput() throws NullPointerException, InstaYakException, IOException {
		InstaYakError test = new InstaYakError(new MessageInput(in));
		assertEquals(test.getOperation(), "ERROR");
	}

	/**
	 * Test for constructor with a MessageInput parameter in InstaYakError
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakErrorMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakError test1 = new InstaYakError(in);
		assertEquals(test1.getOperation(), "ERROR");
		InstaYakError test2 = new InstaYakError(in);
		assertEquals(test2.getOperation(), "ERROR");
		InstaYakError test3 = new InstaYakError(in);
		assertEquals(test3.getOperation(), "ERROR");
	}

	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in
	 * InstaYakError Wrong Operation Exception
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException {
		new InstaYakError(new MessageInput(inWrongOperation));
	}

	/**
	 * Test toString in InstaYakError
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testToString() throws InstaYakException {
		InstaYakError test = new InstaYakError("Bad stuff");
		assertEquals(test.toString(), "Error: Message=Bad stuff");
	}

	/**
	 * Test getOperation in InstaYakError
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testGetOperation() throws InstaYakException {
		InstaYakError test = new InstaYakError("Bad stuff");
		assertEquals(test.getOperation(), "ERROR");
	}

	/**
	 * Test for getID in InstaYakID
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testGetMessage() throws InstaYakException {
		InstaYakError test = new InstaYakError("Bad");
		assertEquals(test.getMessage(), "Bad");
	}

	/**
	 * Test for setID in InstaYakID
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testSetMessage() throws InstaYakException {
		InstaYakError test = new InstaYakError("Bad");
		test.setMessage("Bad stuff");
		assertEquals(test.getMessage(), "Bad stuff");
	}

	/**
	 * Test encode in InstaYakError
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testEncode() throws IOException, InstaYakException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MessageOutput mout = new MessageOutput(bout);
		new InstaYakError("Somthing wrong").encode(mout);
		new InstaYakError("Somthing wrong2").encode(mout);
		new InstaYakError("Somthing wrong 3").encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}

	/**
	 * Test encoding throwing exception in InstaYakError
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = NullPointerException.class)
	public void testEncodeException() throws IOException, InstaYakException {
		MessageOutput mout = new MessageOutput(null);
		new InstaYakError("Bad stuff").encode(mout);
	}

	/**
	 * Test equals in InstaYakError
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testEquals() throws InstaYakException {
		InstaYakError test1 = new InstaYakError("Bad stuff");
		InstaYakError test2 = new InstaYakError("Bad stuff");
		assertTrue(test1.equals(test2));
	}

	/**
	 * Test not equal in InstaYakError
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testNotEquals() throws InstaYakException {
		InstaYakError test1 = new InstaYakError("Bad stuff");
		InstaYakError test2 = new InstaYakError("Good stuff");
		assertFalse(test1.equals(test2));
	}
    
	/**
	 * Test equals in InstaYakError
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testhashCode() throws InstaYakException {
		InstaYakError test1 = new InstaYakError("Bad stuff");
		InstaYakError test2 = new InstaYakError("Bad stuff");
		assertTrue(test1.hashCode() == test2.hashCode());
	}
}

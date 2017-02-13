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
import instayak.serialization.InstaYakSLMD;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakSLMD class
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakSLMDTest {
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
	private InputStream inputStreamSLMD;

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
			encoding = "SLMD\r\n".getBytes("ISO8859-1");
			encodingMultiLines = "SLMD\r\nSLMD\r\nSLMD\r\n".getBytes("ISO8859-1");
			encodingWrongOperation = "ID 123\r\n".getBytes("ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}

		inputStreamSLMD = new ByteArrayInputStream(encoding);
		inMultiLines = new ByteArrayInputStream(encodingMultiLines);
		inWrongOperation = new ByteArrayInputStream(encodingWrongOperation);
	}

	/**
	 * Test default constructor
	 */
	@Test
	public void testDefaultConstructor() {
		InstaYakSLMD test = new InstaYakSLMD();
		assertEquals(test.getOperation(), "SLMD");
	}

	/**
	 * Test constructor with a MessageInput parameter in InstaYakSLMD
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakSLMDMessageInput() throws NullPointerException, InstaYakException, IOException {
		InstaYakSLMD test = new InstaYakSLMD(new MessageInput(inputStreamSLMD));
		assertEquals(test.getOperation(), "SLMD");
	}

	/**
	 * Test for constructor with a MessageInput parameter in InstaYakSLMD
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakSLMDMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput messageInput = new MessageInput(inMultiLines);
		InstaYakSLMD test1 = new InstaYakSLMD(messageInput);
		assertEquals(test1.getOperation(), "SLMD");
		InstaYakSLMD test2 = new InstaYakSLMD(messageInput);
		assertEquals(test2.getOperation(), "SLMD");
		InstaYakSLMD test3 = new InstaYakSLMD(messageInput);
		assertEquals(test3.getOperation(), "SLMD");
	}

	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in
	 * InstaYakSLMD Wrong Operation Exception
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException {
		new InstaYakSLMD(new MessageInput(inWrongOperation));
	}

	/**
	 * Test toString in InstaYakSLMD
	 */
	@Test
	public void testToString() {
		InstaYakSLMD test = new InstaYakSLMD();
		assertEquals(test.toString(), "SLMD");
	}

	/**
	 * Test getOperation in InstaYakSLMD
	 */
	@Test
	public void testGetOperation() {
		InstaYakSLMD test = new InstaYakSLMD();
		assertEquals(test.getOperation(), "SLMD");
	}

	/**
	 * Test encode in InstaYakSLMD
	 * 
	 * @throws IOException
	 *             If I/O problems
	 */
	@Test
	public void testEncode() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MessageOutput mout = new MessageOutput(bout);
		new InstaYakSLMD().encode(mout);
		new InstaYakSLMD().encode(mout);
		new InstaYakSLMD().encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}

	/**
	 * Test encoding throwing exception in InstaYakSLMD
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = NullPointerException.class)
	public void testEncodeException() throws IOException, InstaYakException {
		MessageOutput mout = new MessageOutput(null);
		new InstaYakSLMD().encode(mout);
	}

	/**
	 * Test equals in InstaYakSLMD
	 */
	@Test
	public void testEquals() {
		InstaYakSLMD test1 = new InstaYakSLMD();
		InstaYakSLMD test2 = new InstaYakSLMD();
		assertTrue(test1.equals(test2));
	}
	
	/**
	 * Test hashCode in InstaYakSLMD
	 */
	@Test
	public void testhashCode() {
		InstaYakSLMD test1 = new InstaYakSLMD();
		InstaYakSLMD test2 = new InstaYakSLMD();
		assertTrue(test1.hashCode() == test2.hashCode());
	}

}

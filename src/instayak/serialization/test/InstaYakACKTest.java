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
import instayak.serialization.InstaYakMessage;
import instayak.serialization.InstaYakSLMD;
import instayak.serialization.InstaYakACK;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakACK class
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakACKTest {
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
	 * Initializing the test data
	 */
	@Before
	public void prepare() {
		try {
			encoding = "ACK\r\n".getBytes(InstaYakMessage.PROTOCOL);
			encodingMultiLines = "ACK\r\nACK\r\nACK\r\n".getBytes(InstaYakMessage.PROTOCOL);
			encodingWrongOperation = "ID 123\r\n".getBytes(InstaYakMessage.PROTOCOL);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}

		in = new ByteArrayInputStream(encoding);
		inMultiLines = new ByteArrayInputStream(encodingMultiLines);
		inWrongOperation = new ByteArrayInputStream(encodingWrongOperation);
	}

	/**
	 * Test default constructor
	 */
	@Test
	public void testDefaultConstructor() {
		InstaYakACK test = new InstaYakACK();
		assertEquals(test.getOperation(), "ACK");
	}

	/**
	 * Test constructor with a MessageInput parameter in InstaYakACK
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakACKMessageInput() throws NullPointerException, InstaYakException, IOException {
		InstaYakACK test = new InstaYakACK(new MessageInput(in));
		assertEquals(test.getOperation(), "ACK");
	}

	/**
	 * Test for constructor with a MessageInput parameter in InstaYakACK
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakACKMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakACK test1 = new InstaYakACK(in);
		assertEquals(test1.getOperation(), "ACK");
		InstaYakACK test2 = new InstaYakACK(in);
		assertEquals(test2.getOperation(), "ACK");
		InstaYakACK test3 = new InstaYakACK(in);
		assertEquals(test3.getOperation(), "ACK");
	}

	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in
	 * InstaYakACK Wrong Operation Exception
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException {
		new InstaYakACK(new MessageInput(inWrongOperation));
	}

	/**
	 * Test toString in InstaYakACK
	 */
	@Test
	public void testToString() {
		InstaYakACK test = new InstaYakACK();
		assertEquals(test.toString(), "ACK");
	}

	/**
	 * Test getOperation in InstaYakACK
	 */
	@Test
	public void testGetOperation() {
		InstaYakACK test = new InstaYakACK();
		assertEquals(test.getOperation(), "ACK");
	}

	/**
	 * Test encode in InstaYakACK
	 * 
	 * @throws IOException
	 *             If I/O problems
	 */
	@Test
    public void testEncode() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		MessageOutput mout = new MessageOutput(bout);
		new InstaYakACK().encode(mout);
		new InstaYakACK().encode(mout);
		new InstaYakACK().encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}

	/**
	 * Test encoding throwing exception in InstaYakACK
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = NullPointerException.class)
	public void testEncodeException() throws IOException, InstaYakException {
		MessageOutput mout = new MessageOutput(null);
		new InstaYakACK().encode(mout);
	}

	/**
	 * Test equals in InstaYakACK
	 */
	@Test
	public void testEquals() {
		InstaYakACK test1 = new InstaYakACK();
		InstaYakACK test2 = new InstaYakACK();
		assertTrue(test1.equals(test2));
	}
	
	/**
	 * Test equals in InstaYakACK
	 */
	@Test
	public void testNotEquals() {
		InstaYakACK test1 = new InstaYakACK();
		InstaYakSLMD test2 = new InstaYakSLMD ();
		assertFalse(test1.equals(test2));
	}
	
	/**
	 * Test hashCode in InstaYakACK
	 */
	@Test
	public void testHashCode(){
		InstaYakACK test1 = new InstaYakACK();
		InstaYakACK test2 = new InstaYakACK();
		assertTrue(test1.hashCode() == test2.hashCode());
	}
}

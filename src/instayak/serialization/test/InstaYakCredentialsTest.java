/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import instayak.serialization.InstaYakCredentials;
import instayak.serialization.InstaYakException;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakCredentials class
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakCredentialsTest {
	/**
	 * constant protocol information
	 */
	private static final String PROTOCOL = "ISO8859-1";

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
	 * the String for encoding test with multiple input lines
	 */
	private String encodingMultiLinesString;

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
			encoding = "CRED 12341234123412341234123412341234\r\n".getBytes(PROTOCOL);

			encodingMultiLinesString = "CRED 14AB14AB14AB14AB14AB14AB14AB14AB\r\n"
					+ "CRED 12341234123412341234123412341234\r\n" + "CRED A56CA56CA56CA56CA56CA56CA56CA56C\r\n";
			encodingMultiLines = encodingMultiLinesString.getBytes(PROTOCOL);

			encodingWrongOperation = "ID 1234\r\n".getBytes(PROTOCOL);
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
	 *             if validation error.
	 */
	@Test
	public void testConstructor() throws InstaYakException {
		InstaYakCredentials test = new InstaYakCredentials("14AB14AB14AB14AB14AB14AB14AB14AB");
		assertEquals(test.getHash(), "14AB14AB14AB14AB14AB14AB14AB14AB");
	}

	/**
	 * Test constructor with a MessageInput parameter in InstaYakCredentials
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakCredentialsMessageInput() throws NullPointerException, InstaYakException, IOException {
		InstaYakCredentials test = new InstaYakCredentials(new MessageInput(in));
		assertEquals(test.getHash(), "12341234123412341234123412341234");
	}

	/**
	 * Test for constructor with a MessageInput parameter in InstaYakCredentials
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakErrorMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakCredentials test1 = new InstaYakCredentials(in);
		assertEquals(test1.getHash(), "14AB14AB14AB14AB14AB14AB14AB14AB");
		InstaYakCredentials test2 = new InstaYakCredentials(in);
		assertEquals(test2.getHash(), "12341234123412341234123412341234");
		InstaYakCredentials test3 = new InstaYakCredentials(in);
		assertEquals(test3.getHash(), "A56CA56CA56CA56CA56CA56CA56CA56C");
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
		new InstaYakCredentials(new MessageInput(inWrongOperation));
	}

	/**
	 * Test toString in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testToString() throws InstaYakException {
		InstaYakCredentials test = new InstaYakCredentials("12341234123412341234123412341234");
		assertEquals(test.toString(), "Credentials: Hash=12341234123412341234123412341234");
	}

	/**
	 * Test getOperation in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testGetOperation() throws InstaYakException {
		InstaYakCredentials test = new InstaYakCredentials("12341234123412341234123412341234");
		assertEquals(test.getOperation(), "CRED");
	}

	/**
	 * Test for getHash in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testGetHash() throws InstaYakException {
		InstaYakCredentials test = new InstaYakCredentials("14AB14AB14AB14AB14AB14AB14AB14AB");
		assertEquals(test.getHash(), "14AB14AB14AB14AB14AB14AB14AB14AB");
	}

	/**
	 * Test for setHash in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testSetHash() throws InstaYakException {
		InstaYakCredentials test = new InstaYakCredentials("14AB14AB14AB14AB14AB14AB14AB14AB");
		test.setHash("12341234123412341234123412341234");
		assertEquals(test.getHash(), "12341234123412341234123412341234");
	}

	/**
	 * Test encode in InstaYakCredentials
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
		new InstaYakCredentials("14AB14AB14AB14AB14AB14AB14AB14AB").encode(mout);
		new InstaYakCredentials("12341234123412341234123412341234").encode(mout);
		new InstaYakCredentials("A56CA56CA56CA56CA56CA56CA56CA56C").encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}

	/**
	 * Test encoding throwing exception in InstaYakCredentials
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = NullPointerException.class)
	public void testEncodeException() throws IOException, InstaYakException {
		MessageOutput mout = new MessageOutput(null);
		new InstaYakCredentials("12341234123412341234123412341234").encode(mout);
	}

	/**
	 * Test equals in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testEquals() throws InstaYakException {
		InstaYakCredentials test1 = new InstaYakCredentials("12341234123412341234123412341234");
		InstaYakCredentials test2 = new InstaYakCredentials("12341234123412341234123412341234");
		assertTrue(test1.equals(test2));
	}

	/**
	 * Test not equal in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testNotEquals() throws InstaYakException {
		InstaYakCredentials test1 = new InstaYakCredentials("12341234123412341234123412341234");
		InstaYakCredentials test2 = new InstaYakCredentials("14AB14AB14AB14AB14AB14AB14AB14AB");
		assertFalse(test1.equals(test2));
	}
	
	/**
	 * Test hashCode in InstaYakCredentials
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testhashCode() throws InstaYakException {
		InstaYakCredentials test1 = new InstaYakCredentials("12341234123412341234123412341234");
		InstaYakCredentials test2 = new InstaYakCredentials("12341234123412341234123412341234");
		assertTrue(test1.hashCode() == test2.hashCode());
	}
}

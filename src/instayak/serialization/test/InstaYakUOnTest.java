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
import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakUOn;
import instayak.serialization.MessageInput;
import instayak.serialization.MessageOutput;

/**
 * Test for InstaYakUOn class
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
public class InstaYakUOnTest {
	/**
	 * constant protocol information
	 */
	private static final String PROTOCOL = "ISO8859-1";
    
	/**
	 * the byte array of original image
	 */
	byte[] image = new byte[] {0x7F, (byte) 0xC8, 0x23, (byte) 0xB3, 0x5};

	
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
			String testString = "UOn testCate123 " + Base64.getEncoder().withoutPadding().encodeToString(image) +"\r\n";
			encoding = testString.getBytes(PROTOCOL);

			encodingMultiLines = (testString+testString+testString).getBytes(PROTOCOL);

			encodingWrongOperation = "ID 1234\r\n".getBytes(PROTOCOL);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}

		in = new ByteArrayInputStream(encoding);
		inMultiLines = new ByteArrayInputStream(encodingMultiLines);
		inWrongOperation = new ByteArrayInputStream(encodingWrongOperation);
	}

	/**
	 * Test constructor with String and byte[] parameters
	 * 
	 * @throws InstaYakException
	 *             if validation error.
	 */
	@Test
	public void testConstructor() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("testCate123", image);
		assertEquals(test.getOperation(), InstaYakUOn.OPERATION);
	}

	/**
	 * Test constructor with a MessageInput parameter in InstaYakUOn
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakCredentialsMessageInput() throws NullPointerException, InstaYakException, IOException {
		InstaYakUOn test = new InstaYakUOn(new MessageInput(in));
		assertEquals(test.getCategory(), "testCate123");
	}

	/**
	 * Test for constructor with a MessageInput parameter in InstaYakInstaYakUOn
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test
	public void testInstaYakUOnMessageInputMultilines() throws InstaYakException, IOException {
		MessageInput in = new MessageInput(inMultiLines);
		InstaYakUOn test1 = new InstaYakUOn(in);
		assertEquals(test1.getCategory(), "testCate123");
		InstaYakUOn test2 = new InstaYakUOn(in);
		assertEquals(test2.getCategory(), "testCate123");
		InstaYakUOn test3 = new InstaYakUOn(in);
		assertArrayEquals(test3.getImage(), image);
	}

	/**
	 * Test for constructor with a MessageInput parameter throwing Exception in
	 * InstaYakUOn
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionWrongOperation() throws InstaYakException, IOException {
		new InstaYakUOn(new MessageInput(inWrongOperation));
	}

	/**
	 * Test toString in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testToString() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("testCate123", image);
		assertEquals(test.toString(), "UOn: Category=testCate123 Image=5 bytes");
	}

	/**
	 * Test getOperation in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testGetOperation() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("testCate12", image);
		assertEquals(test.getOperation(), "UOn");
	}

	/**
	 * Test for getCategory in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testGetCategory() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("testCate123", image);
		assertEquals(test.getCategory(), "testCate123");
	}
	
	/**
	 * Test for getImage in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testGetImage() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("test", image);
		assertEquals(test.getImage(), image);
	}

	/**
	 * Test for setCategory in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testSetCategory() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("test1", image);
		test.setCategory("test2");
		assertEquals(test.getCategory(), "test2");
	}
	
	/**
	 * Test for setImage in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test
	public void testSetImage() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("test1", image);
		byte[] image2 = new byte[] {0x7F, (byte) 0xC9, 0x24, (byte) 0xB4, 0x6};
		test.setImage(image2);
		assertArrayEquals(test.getImage(), image2);
	}
	
	/**
	 * Test for setImage in InstaYakUOn throwing exception
	 * 
	 * @throws InstaYakException
	 *             if validation failure
	 */
	@Test(expected=InstaYakException.class)
	public void testSetImageNullImage() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("test1", image);
		test.setImage(null);
	}

	/**
	 * Test encode in InstaYakUOn
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
		new InstaYakUOn("testCate123", image).encode(mout);
		new InstaYakUOn("testCate123", image).encode(mout);
		new InstaYakUOn("testCate123", image).encode(mout);
		assertArrayEquals(encodingMultiLines, bout.toByteArray());
	}

	/**
	 * Test encoding throwing exception in InstaYakUOn
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = NullPointerException.class)
	public void testEncodeException() throws IOException, InstaYakException {
		MessageOutput mout = new MessageOutput(null);
		new InstaYakUOn("testCate456", image).encode(mout);
	}

	/**
	 * Test equals in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testEquals() throws InstaYakException {
		InstaYakUOn test1 = new InstaYakUOn("testCate456", image);
		InstaYakUOn test2 = new InstaYakUOn("testCate456", image);
		assertTrue(test1.equals(test2));
	}

	/**
	 * Test not equal in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testNotEquals() throws InstaYakException {
		InstaYakUOn test1 = new InstaYakUOn("firstTest", image);
		InstaYakUOn test2 = new InstaYakUOn("testCate", image);
		assertFalse(test1.equals(test2));
	}
	
	/**
	 * Test hashCode in InstaYakUOn
	 * 
	 * @throws InstaYakException
	 *             If validation failure
	 */
	@Test
	public void testhashCode() throws InstaYakException {
		InstaYakUOn test1 = new InstaYakUOn("testCate456", image);
		InstaYakUOn test2 = new InstaYakUOn("testCate456", image);
		assertTrue(test1.hashCode() == test2.hashCode());
	}
}

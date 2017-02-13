/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakUOn;
import instayak.serialization.MessageInput;

/**
 * Test for InstaYakCredentials class throwing exception in constructor
 *     with a MessageInput as parameter
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakUOnTestOnConstructorInvalidMessageInput {
	/**
	 * the inputStream for encoding test invalid error message
	 */
	@Parameter(value = 0)
	public byte[] encodingValidationError;
	
	/**
	 * the byte array of original image
	 */
	public static byte[] image = new byte[] {0x7F, (byte) 0xC8, 0x23, (byte) 0xB3, 0x5};
	
	/**
	 * Initializing the invalid message
	 * 
	 * @return the collection of invalid message
	 */
	@Parameters
	public static Collection<Object[]> data() {
		String imgString = Base64.getEncoder().withoutPadding().encodeToString(image);
		try {
			return Arrays.asList(new Object[][] {
			    { "UOn \r\n".getBytes("ISO8859-1") }, 
			    { "UOn 14\r\n".getBytes("ISO8859-1") },
			    { "UOn 14 \r\n".getBytes("ISO8859-1") },
		        { ("UOn 12" + imgString + "\r\n").getBytes("ISO8859-1") }, 
		        { ("UOn 12 " + imgString + "\r\r\n").getBytes("ISO8859-1") }, 
		        { ("UOn " + imgString + "\r\n").getBytes("ISO8859-1") }
			});
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception
	 * in InstaYakUOn
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws InstaYakException, IOException {
		InputStream validationError = new ByteArrayInputStream(encodingValidationError);
		new InstaYakUOn(new MessageInput(validationError));
	}
}

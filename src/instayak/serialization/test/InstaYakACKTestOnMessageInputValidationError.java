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
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import instayak.serialization.InstaYakACK;
import instayak.serialization.InstaYakException;
import instayak.serialization.MessageInput;

/**
 * Test for InstaYakACK class throwing exception in constructor
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakACKTestOnMessageInputValidationError {
	/**
	 * constant protocol information
	 */
	private final static String PROTOCOL = "ISO8859-1";
	
	/**
	 * the inputStream for encoding test invalid version
	 */
	@Parameter(value = 0)
	public byte[] encodingValidationError;
    
	/**
	 * Create the test data
	 * @return the collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		try {
			return Arrays.asList(new Object[][] { 
			    { "ACK \r\n".getBytes(PROTOCOL) }, 
			    { " ACK\r\n".getBytes(PROTOCOL) },
			    { "A CK\r\n".getBytes(PROTOCOL) },
		        { "ACK \n".getBytes(PROTOCOL) }, 
		        { "ACK\r".getBytes(PROTOCOL) },
		        { " ACK\r\n".getBytes(PROTOCOL) }, 
		        { " ACK\r\r\n".getBytes(PROTOCOL) },
		        { "ACK".getBytes(PROTOCOL) },
		        { "\r\n".getBytes(PROTOCOL) }
		    });
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception
	 * in InstaYakACK Validation Error Exception
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws InstaYakException, IOException {
		InputStream validationError = new ByteArrayInputStream(encodingValidationError);
		new InstaYakACK(new MessageInput(validationError));
	}
}

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

import instayak.serialization.InstaYakError;
import instayak.serialization.InstaYakException;
import instayak.serialization.MessageInput;

/**
 * Test for constructor in InstaYakError class with invalid MessageInput
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(value = Parameterized.class)
public class InstaYakErrorTestOnConstructorInvalidMessageInput {
	/**
	 * the inputStream for encoding test invalid error message
	 */
	@Parameter(value = 0)
	public byte[] encodingValidationError;
    
	/**
	 * Create test data
	 * @return the collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		try {
			return Arrays.asList(new Object[][] { 
			    { "ERROR \r\n".getBytes("ISO8859-1") }, 
			    { "ERR OR \r\n".getBytes("ISO8859-1") },
			    { "ERROR\r\n".getBytes("ISO8859-1") },
		        { "ERROR *\n".getBytes("ISO8859-1") }, 
		        { "ERROR 1*2\r".getBytes("ISO8859-1") },
		        { "ERROR 12\n".getBytes("ISO8859-1") }, 
		        { "ERROR 12\r\r\n".getBytes("ISO8859-1") },
		        { "ERROR 12".getBytes("ISO8859-1") },
		        { "ERROR 12 12*".getBytes("ISO8859-1") }
		    });
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception
	 * in InstaYakError Validation Error Exception
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws InstaYakException, IOException {
		InputStream validationError = new ByteArrayInputStream(encodingValidationError);
		new InstaYakError(new MessageInput(validationError));
	}
	
}

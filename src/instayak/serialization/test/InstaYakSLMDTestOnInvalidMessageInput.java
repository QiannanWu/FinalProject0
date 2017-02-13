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

import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakSLMD;
import instayak.serialization.MessageInput;

/**
 * Test for InstaYakSLMD class throwing exception in constructor
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakSLMDTestOnInvalidMessageInput {
	/**
	 * byte array containing invalid message
	 */
	@Parameter(value = 0)
	public byte[] encodingValidationError;
    
	/**
	 * Create test data
	 * @return collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		try {
			return Arrays.asList(new Object[][] { 
			    { "SLMD \r\n".getBytes("ISO8859-1") }, 
			    { "SLMD 123\r\n".getBytes("ISO8859-1") },
			    { "SL MD \r\n".getBytes("ISO8859-1") },
			    { "S LMD\r\n".getBytes("ISO8859-1") },
		        { "SLMD \n".getBytes("ISO8859-1") }, 
		        { "SLMD\r".getBytes("ISO8859-1") },
		        { " SLMD\r\n".getBytes("ISO8859-1") }, 
		        { " SLMD\r\r\n".getBytes("ISO8859-1") },
		        { "SLMD".getBytes("ISO8859-1") },
		        { "\r\n".getBytes("ISO8859-1") }
		    });
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception
	 * in InstaYakSLMD Validation Error Exception
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testMessageInputExceptionValidationError() throws InstaYakException, IOException {
		InputStream validationError = new ByteArrayInputStream(encodingValidationError);
		new InstaYakSLMD(new MessageInput(validationError));
	}
}

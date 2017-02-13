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

import instayak.serialization.InstaYakUOn;
import instayak.serialization.InstaYakException;
import instayak.serialization.MessageInput;

/**
 * Test for InstaYakUOn class throwing exception in constructor
 *     with a MessageInput as parameter
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakCredentialsTestOnConstructorInvalidMessageInput {
	/**
	 * the inputStream for encoding test invalid error message
	 */
	@Parameter(value = 0)
	public byte[] encodingValidationError;
    
	/**
	 * Initializing the invalid message
	 * 
	 * @return the collection of invalid message
	 */
	@Parameters
	public static Collection<Object[]> data() {
		try {
			return Arrays.asList(new Object[][] { 
			    { "CRED \r\n".getBytes("ISO8859-1") }, 
			    { "CRE D 14AB14AB14AB14AB14AB14AB14AB14AB\r\n".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14A\r\n".getBytes("ISO8859-1") }, 
		        { "CRED 14ab14AB14AB14AB14AB14AB14AB14AB\r\n".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14A*\r\n".getBytes("ISO8859-1") }, 
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14AB\r\r\n".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14AB".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14AB\r".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14AB\n".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14GH\r\n".getBytes("ISO8859-1") },
		        { "CRED 14AB14AB14AB14AB14AB14AB14AB14ABC\r\n".getBytes("ISO8859-1") }
			});
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
	}
	
	/**
	 * Test for constructor with a MessageInput parameter throwing Exception
	 * in InstaYakCredentials
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

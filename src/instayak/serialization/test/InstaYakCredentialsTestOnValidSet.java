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

import instayak.serialization.InstaYakCredentials;
import instayak.serialization.InstaYakException;
import instayak.serialization.MessageInput;

/**
 * Test setHash method with valid String in InstaYakCredentials
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakCredentialsTestOnValidSet {
	/**
	 * the inputStream for encoding test valid error message
	 */
	@Parameter(value = 0)
	public byte[] encoding;
	
	/**
	 * Initializing the invalid Strings
	 * 
	 * @return the collection of invalid Strings
	 */
	@Parameters
	public static Collection<Object[]> data() {
		try {
			return Arrays.asList(new Object[][] { 
			    { "CRED 14AB14AB14AB14AB14AB14AB14AB14AB\r\n".getBytes("ISO8859-1") }, 
			    { "CRED 14EF00AB8914AB14AB14AB14AB14AB89\r\n".getBytes("ISO8859-1") }
		    });
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unable to encode", e);
		}
	}
		
	/**
	 * Test for setHash with valid hash strings
	 *     
	 * @throws InstaYakException if validation failure
	 * @throws IOException if I/O problem
	 * @throws NullPointerException if validation fails
	 */
	@Test
	public void testSetHash() throws InstaYakException, NullPointerException, IOException {
		InputStream validMessage = new ByteArrayInputStream(encoding);
		new InstaYakCredentials(new MessageInput(validMessage));
	}
}

/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization.test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import instayak.serialization.InstaYakError;
import instayak.serialization.InstaYakException;

/**
 * Test for constructor in InstaYakError class with invalid String 
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(value = Parameterized.class)
public class InstaYakErrorTestOnConstructorInvalidString {
	/**
	 * the inputStream for encoding test invalid error message
	 */
	@Parameter(value = 0)
	public String input;
    
	/**
	 * Create test data
	 * @return the Collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
		    { "" }, 
		    { null },
		    { "*"}, 
		    { "1*2" },
		    { "12 12AX\n" }
		});
	}
	
	/**
	 * Test for constructor with a String parameter throwing Exception
	 * in InstaYakError
	 * 
	 * @throws IOException
	 *             If I/O problems
	 * @throws InstaYakException
	 *             If unable to parse or validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testStringConstructorExceptionValidationError() throws InstaYakException, IOException {
		new InstaYakError(input);
	}
	
}

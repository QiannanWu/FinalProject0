/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization.test;

import static org.junit.Assert.*;

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
 * Test for setMessage in InstaYakError with valid Strings
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakErrorTestOnValidSet {
	/**
	 * the valid error message strings
	 */
	@Parameter(value = 0)
	public String message;
    
	/**
	 * Create test data
	 * @return the collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
		    { " " }, 
		    { "     " },
		    { "12      34" },
		    { "12 asdfasdf" }
		});
	}
		
	/**
	 * Test for setMessage
	 *     
	 * @throws InstaYakException if validation failure
	 * @throws IOException if I/O problem
	 * @throws NullPointerException if validation fails
	 */
	@Test
	public void testSetIDLength() throws InstaYakException, NullPointerException, IOException {
		InstaYakError test = new InstaYakError("Bad stuff");
		test.setMessage(message);
	    assertEquals(test.getMessage(), message);
	}
}

/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization.test;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import instayak.serialization.InstaYakCredentials;
import instayak.serialization.InstaYakException;

/**
 * Test for InstaYakCredentials class throwing exception in constructor
 *     with a String as parameter
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakCredentialsTestOnConstructorInvalidString {
	/**
	 * the invalid string for testing constructor
	 */
	@Parameter(value = 0)
	public String message;
    
	/**
	 * Initializing the invalid Strings
	 * 
	 * @return the collection of invalid Strings
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
			{ "" }, 
		    { "14AB14AB14AB14AB14AB14AB14AB14A" },
	        { "14AB14AB14AB14AB14AB14AB14AB14A*" },
	        { "14AB14AB14AB14AB14AB14AB14AB14ab" }, 
	        { "14AB14AB14AB14AB14AB14AB14AB14ZY" },
	        { "14AB14AB 14AB14AB14AB14AB14AB14A" },
		});
	}
	
	/**
	 * Test for constructor throwing exception in InstaYakCredentials
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testConstructor() throws InstaYakException {
		new InstaYakCredentials(message);
	}

}

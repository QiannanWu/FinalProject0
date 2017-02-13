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
 * Test for InstaYakCredentials class throwing exception in setHash method
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakCredentialsTestOnInvalidSet {
	/**
	 * the invalid string for testing setHash in InstaYakCredentials
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
			{null},
			{ "" }, 
		    { "14AB14AB14AB14AB14AB14AB14AB14A" },
	        { "14AB14AB14AB14AB14AB14AB14AB14A*" },
	        { "14AB14AB14AB14AB14AB14AB14AB14ab" }, 
	        { "14AB14AB14AB14AB14AB14AB14AB14ZY" },
	        { "14AB14AB 14AB14AB14AB14AB14AB14A" },
		});
	}
	
	/**
	 * Test for setHash throwing exception in InstaYakCredentials
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetIDLength() throws InstaYakException {
		InstaYakCredentials test = new InstaYakCredentials("14AB14AB14AB14AB14AB14AB14AB14AB");
		test.setHash(message);
	}

}

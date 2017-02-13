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

import instayak.serialization.InstaYakError;
import instayak.serialization.InstaYakException;

/**
 * Test for setMessage in InstaYakError throwing exception
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(value = Parameterized.class)
public class InstaYakErrorTestOnInvalidSetting {
	/** 
	 * the invalid string for testing setMessage in InstaYakError
	 */
	@Parameter(value = 0)
	public String message;
    
	/**
	 * Create test data
	 * @return collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
		    { "" }, 
		    { "*" }, 
		    { "1*a 1" }
		});
	}
	
	/**
	 * Test for setMessage throwing exception in InstaYakError
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetMessage() throws InstaYakException {
		InstaYakError test = new InstaYakError("Bad");
		test.setMessage(message);
	}

}

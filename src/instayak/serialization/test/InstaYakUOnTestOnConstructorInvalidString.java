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

import instayak.serialization.InstaYakUOn;
import instayak.serialization.InstaYakException;

/**
 * Test for InstaYakUOn class throwing exception in constructor
 *     with a String as parameter
 *     
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(Parameterized.class)
public class InstaYakUOnTestOnConstructorInvalidString {
	/**
	 * the invalid string for testing constructor
	 */
	@Parameter(value = 0)
	public String category;
    
	/**
	 * the byte array of original image
	 */
	public static byte[] image = new byte[] {0x7F, (byte) 0xC8, 0x23, (byte) 0xB3, 0x5};
	
	/**
	 * Initializing the invalid Strings
	 * 
	 * @return the collection of invalid Strings
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
			{ "" }, 
		    { "14 12" },
	        { "1A3*123" },
	        { "1412abc\n" }, 
		});
	}
	
	/**
	 * Test for constructor throwing exception in InstaYakUOn
	 * 
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetIDLength() throws InstaYakException {
		new InstaYakUOn(category, image);
	}

}

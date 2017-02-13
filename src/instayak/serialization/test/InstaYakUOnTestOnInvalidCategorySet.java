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

import instayak.serialization.InstaYakException;
import instayak.serialization.InstaYakUOn;

/**
 * Test for setCategory in InstaYakUOn throwing exception
 * 
 * @version 1.0 28 January 2017
 * @author Qiannan Wu
 */
@RunWith(value = Parameterized.class)
public class InstaYakUOnTestOnInvalidCategorySet {
	/** 
	 * the invalid string for testing setCategory in InstaYakUOn
	 */
	@Parameter(value = 0)
	public String category;
    
	/**
	 * Create test data
	 * @return collection of test data
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { 
		    { "" }, 
		    { null },
		    { "*" }, 
		    { "1*a1" },
		    { "0x1 " }
		});
	}
	
	
	/**
	 * the byte array of original image
	 */
	static byte[] image = new byte[] {0x7F, (byte) 0xC8, 0x23, (byte) 0xB3, 0x5};
	
	/**
	 * Test for setCategory throwing exception in InstaYakUOn
	 *     
	 * @throws InstaYakException if validation failure
	 */
	@Test(expected = InstaYakException.class)
	public void testSetCategory() throws InstaYakException {
		InstaYakUOn test = new InstaYakUOn("test", image);
		test.setCategory(category);
	}

}

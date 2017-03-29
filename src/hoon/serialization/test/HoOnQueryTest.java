/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization.test;


import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hoon.serialization.ErrorCode;
import hoon.serialization.HoOnException;
import hoon.serialization.HoOnMessage;
import hoon.serialization.HoOnQuery;
/**
 * Test for HoOnQuery class
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public class HoOnQueryTest {
	/**
	 * the byte[] array for test
	 */
	private byte[] encoding;
	
	/**
	 * prepare a encoding package
	 */
	@Before
	public void prepare() {
		ByteBuffer in = MappedByteBuffer.allocate(HoOnMessage.MAX_SIZE_QUERY);
		in.put((byte) 32);
		in.put((byte) 0); // no error
		in.putInt(123);
		in.putShort((short) 1);
		encoding = in.array();
	}
	
	/**
	 * Test hashCode method
	 */
	@Test
	public void testHashCode() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		HoOnQuery q2 = new HoOnQuery(queryId, requestedPosts);
		
		assertTrue(q1.hashCode() == q2.hashCode());
	}
	
    /**
     * Test setQueryId method
     * @throws IllegalAccessException if query Id is not valid
     */
	@Test
	public void testSetQueryId() throws IllegalAccessException {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		q1.setQueryId(1);
	    assertTrue(q1.getQueryId() == 1);
	}
    
	/**
	 * Test get queryId method
	 */
	@Test
	public void testGetQueryId() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
	    assertTrue(q1.getQueryId() == HoOnMessage.MAX_ID);
	}
    
	/**
	 * Test encode method
	 */
	@Test
	public void testEncode() {
		fail("not implemented yet");
	}
    
	/**
	 * Test getErrorCode method
	 */
	@Test
	public void testGetErrorCode() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
	    assertTrue(ErrorCode.NOERROR.equals(q1.getErrorCode()));
	}
    
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test
	public void testHoOnQueryByteArray() throws HoOnException, IOException {
		HoOnQuery q = new HoOnQuery(encoding);
		assertTrue(q.getErrorCode() == ErrorCode.NOERROR && q.getQueryId() == 123 && q.getRequestedPosts() == 1);
	}
    
	/**
	 * Test constructor with a long and an integer parameter
	 */
	@Test
	public void testHoOnQueryLongInt() {
		HoOnQuery q = new HoOnQuery(123, 1);
		assertTrue(q.getErrorCode() == ErrorCode.NOERROR && q.getQueryId() == 123 && q.getRequestedPosts() == 1);
	}
    
	/**
	 * Test toString method
	 */
	@Test
	public void testToString() {
		long queryId = 123;
		int requestedPosts = 1;
		String a = "[query ID]: 123, [maximum number of posts]: 1";
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		assertTrue(a.equals(q1.toString()));
	}
    
	/**
	 * Test getRequestedPosts method
	 */
	@Test
	public void testGetRequestedPosts() {
		long queryId = 123;
		int requestedPosts = 1;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		assertTrue(q1.getRequestedPosts() == 1);
	}
    
	/**
	 * Test setRequestedPosts method
	 */
	@Test
	public void testSetRequestedPosts() {
		long queryId = 123;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		q1.setRequestedPosts(1);
		assertTrue(q1.getRequestedPosts() == 1);
	}
    
	/**
	 * Test equals method
	 */
	@Test
	public void testEqualsObject() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		HoOnQuery q2 = new HoOnQuery(queryId, requestedPosts);
		
		Assert.assertTrue(q1.equals(q2));
	}

}

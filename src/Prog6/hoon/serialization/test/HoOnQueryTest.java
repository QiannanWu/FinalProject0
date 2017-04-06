/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 4
* Class: CSI4321
*
************************************************/
package hoon.serialization.test;


import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
	private byte[] encodingQuery;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingResponce;
	
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingNonZeroReserve;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingWrongVersion;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingTooShort;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingInvalidErrorCode;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingInvalidErrorCode2;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingTooLong;
	
	/**
	 * prepare a encoding package
	 * @throws IOException if I/O exception
	 */
	@Before
	public void prepare() throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		long queryId = 1234;
		int requestedPosts = 10;
		out.writeByte(HoOnMessage.QUERY_HEADER);
		
		int errorCodeValue = ErrorCode.NOERROR.getErrorCodeValue();
		out.writeByte(errorCodeValue);
		
		int tmp = (int) (queryId & 0xffffffffL);
		out.writeInt(tmp);
		
		short requestedPostsValue = (short) (requestedPosts & 0xffff);
		out.writeShort(requestedPostsValue);
		
		out.flush();
		
		encodingQuery = b.toByteArray();
		
		b.reset();
		out.writeByte(HoOnMessage.RESPONSE_HEADER);
		byte errorCodeValueByte = (byte) errorCodeValue;
		out.writeByte(errorCodeValueByte);
		tmp = (int) (queryId & 0xffffffffL);
		out.writeInt(tmp);
		short numberOfPost = 0;
		out.writeShort(numberOfPost);
		
        out.flush();
        encodingResponce = b.toByteArray();
        
        b.reset();
        
		out.writeByte(33);
		out.writeByte(errorCodeValue);
		out.writeInt(tmp);
		out.writeShort(requestedPostsValue);
		encodingNonZeroReserve = b.toByteArray();
		
		b.reset();
	        
		out.writeByte(48); //001100000
		out.writeByte(errorCodeValue);
		out.writeInt(tmp);
		out.writeShort(requestedPostsValue);
		encodingWrongVersion = b.toByteArray();
		
		b.reset();
        
		out.writeByte(32); //001100000
		out.writeByte(errorCodeValue);
		out.writeInt(tmp);
		encodingTooShort = b.toByteArray();
		
        b.reset();
        
		out.writeByte(32); //001100000
		out.writeByte(8);
		out.writeInt(tmp);
		out.writeShort(requestedPostsValue);
		encodingInvalidErrorCode = b.toByteArray();
		
        b.reset();
        
		out.writeByte(32); //001100000
		out.writeByte(errorCodeValue);
		out.writeInt(tmp);
		out.writeShort(requestedPostsValue);
		out.writeShort(requestedPostsValue);
		out.writeShort(requestedPostsValue);
		out.writeShort(requestedPostsValue);
		encodingTooLong = b.toByteArray();
		
        b.reset();
        
		out.writeByte(32); //001100000
		out.writeByte(ErrorCode.BADVERSION.getErrorCodeValue());
		out.writeInt(tmp);
		out.writeShort(requestedPostsValue);
		encodingInvalidErrorCode2 = b.toByteArray();
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
     * Test setQueryId method throw Exception
     * @throws IllegalAccessException if query Id is not valid
     */
	@Test(expected = IllegalArgumentException.class)
	public void testSetQueryIdInvalid() throws IllegalAccessException {
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		long x = HoOnMessage.MAX_ID + 1;
		HoOnQuery q = new HoOnQuery(HoOnMessage.MAX_ID, requestedPosts);
		q.setQueryId(x);
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
	 * @throws IOException if I/O exception
	 */
	@Test
	public void testEncode() throws IOException {
		HoOnQuery q = new HoOnQuery(1234, 10);
		assertArrayEquals(q.encode(), encodingQuery);
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
		HoOnQuery q = new HoOnQuery(encodingQuery);
		assertTrue(q.getErrorCode() == ErrorCode.NOERROR && q.getQueryId() == 1234 && q.getRequestedPosts() == 10);
	}
	
	/**
	 * Test constructor with a byte array parameter - response
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayWrongType() throws HoOnException, IOException {
		new HoOnQuery(encodingResponce);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayNonZeroReserve() throws HoOnException, IOException {
		new HoOnQuery(encodingNonZeroReserve);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayWrongVersion() throws HoOnException, IOException {
		new HoOnQuery(encodingWrongVersion);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayNull() throws HoOnException, IOException {
		new HoOnQuery(null);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayTooShort() throws HoOnException, IOException {
		new HoOnQuery(encodingTooShort);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayTooLong() throws HoOnException, IOException {
		new HoOnQuery(encodingTooLong);
	}
    
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayInvalidErrorCode() throws HoOnException, IOException {
		new HoOnQuery(encodingInvalidErrorCode);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayInvalidErrorCode2() throws HoOnException, IOException {
		new HoOnQuery(encodingInvalidErrorCode2);
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
	 * Test constructor with an invalid id
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnQueryLongIntInvalidID() {
		new HoOnQuery(HoOnMessage.MAX_ID + 1, 1);
	}
	
	/**
	 * Test constructor with an invalid request
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnQueryLongIntInvalidRequest() {
		new HoOnQuery(1, HoOnMessage.MAX_POST_NUMBER + 1);
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
	 * Test setRequestedPosts method
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetRequestedPostsInvalid() {
		long queryId = 123;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		q1.setRequestedPosts(requestedPosts+1);
	}
	
	/**
	 * Test setRequestedPosts method
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetRequestedPostsInvalid2() {
		long queryId = 123;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		q1.setRequestedPosts(-1);
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
	
	/**
	 * Test equals method - query Id not equals
	 */
	@Test
	public void testNotEqualsObject() {
		long queryId = HoOnMessage.MAX_ID;
		long queryId2 = HoOnMessage.MIN_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		HoOnQuery q2 = new HoOnQuery(queryId2, requestedPosts);
		
		Assert.assertFalse(q1.equals(q2));
	}
	
	/**
	 * Test equals method - requestedPosts not equals
	 */
	@Test
	public void testNotEqualsObject2() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		int requestedPosts2 = HoOnMessage.MIN_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		HoOnQuery q2 = new HoOnQuery(queryId, requestedPosts2);
		
		Assert.assertFalse(q1.equals(q2));
	}
	

	/**
	 * Test equals method - null
	 */
	@Test
	public void testNotEqualsObjectNull() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		HoOnQuery q2 = null;
		
		Assert.assertFalse(q1.equals(q2));
	}
	

	/**
	 * Test equals method - null
	 */
	@Test
	public void testEqualsObjectSelf() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		
		Assert.assertTrue(q1.equals(q1));
	}
	
	/**
	 * Test equals method - null
	 */
	@Test
	public void testNotEqualsDifferentObject() {
		long queryId = HoOnMessage.MAX_ID;
		int requestedPosts = HoOnMessage.MAX_POST_NUMBER;
		
		HoOnQuery q1 = new HoOnQuery(queryId, requestedPosts);
		String n = "Not Query";
		Assert.assertFalse(q1.equals(n));
	}

}

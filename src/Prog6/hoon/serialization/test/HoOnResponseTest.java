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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hoon.serialization.ErrorCode;
import hoon.serialization.HoOnException;
import hoon.serialization.HoOnMessage;
import hoon.serialization.HoOnResponse;

/**
 * Test for HoOnResponse class
 * 
 * @version 1.0 21 March 2017
 * @author Qiannan Wu
 */
public class HoOnResponseTest {
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingQuery;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingResponse;
	
	/**
	 * the byte[] array for test
	 */
	private byte[] encodingTooShort;
	
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
		short numberOfPost = 2;
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		out.writeShort(numberOfPost);
		for(String s : posts){
			short lengthOfPost = (short) (s.length() & 0xffff);
			out.writeShort(lengthOfPost);
			out.writeBytes(s);
		}
        out.flush();
        encodingResponse = b.toByteArray();
        
        b.reset();
		out.writeByte(HoOnMessage.RESPONSE_HEADER);
		out.writeByte(errorCodeValueByte);
		out.writeInt(tmp);
		out.writeShort(4);
		for(String s : posts){
			short lengthOfPost = (short) (s.length() & 0xffff);
			out.writeShort(lengthOfPost);
			out.writeBytes(s);
		}
        out.flush();
        encodingTooShort = b.toByteArray();
        
        b.reset();
		out.writeByte(HoOnMessage.RESPONSE_HEADER);
		out.writeByte(errorCodeValueByte);
		out.writeInt(tmp);
		out.writeShort(2);
		List<String> posts2 = new ArrayList<String>();
		posts2.add("abc");
		posts2.add("1234");
		posts2.add("1234");
		posts2.add("1234");
		for(String s : posts2){
			short lengthOfPost = (short) (s.length() & 0xffff);
			out.writeShort(lengthOfPost);
			out.writeBytes(s);
		}
        out.flush();
        encodingTooLong = b.toByteArray();
	}
	
	
	/**
	 * Test hashCode method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testHashCode() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		HoOnResponse r2 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		
		assertTrue(r1.hashCode() == r2.hashCode());
	}
    
	/**
	 * Test setQueryId method
	 * @throws HoOnException if deserialization failure
	 * @throws IllegalAccessException if queryId is not valid
	 */
	@Test
	public void testSetQueryId() throws HoOnException, IllegalAccessException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		r.setQueryId(12345);
		assertTrue(r.getQueryId() == 12345);
	}
	
	/**
	 * Test setQueryId method
	 * @throws HoOnException if deserialization failure
	 * @throws IllegalAccessException if queryId is not valid
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetQueryIdInvalid() throws HoOnException, IllegalAccessException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, HoOnMessage.MAX_ID, posts);
		r.setQueryId(HoOnMessage.MAX_ID + 1);
	}
	
	/**
	 * Test setQueryId method
	 * @throws HoOnException if deserialization failure
	 * @throws IllegalAccessException if queryId is not valid
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetQueryIdInvalid2() throws HoOnException, IllegalAccessException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, HoOnMessage.MAX_ID, posts);
		r.setQueryId(-1);
	}
    
	/**
	 * Test getQueryId method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testGetQueryId() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		assertTrue(r.getQueryId() == 234);
	}
    
	/**
	 * Test encoding method
	 * @throws HoOnException if error during serialization
	 * @throws IOException if I/O exception
	 */
	@Test
	public void testEncode() throws HoOnException, IOException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 1234, posts);
		assertArrayEquals(encodingResponse, r.encode());
	}
    
	/**
	 * Test constructor with a byte array parameter - Query
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayWrongType() throws HoOnException, IOException {
		new HoOnResponse(encodingQuery);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayTooShort() throws HoOnException, IOException {
		new HoOnResponse(encodingTooShort);
	}
	
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test(expected = HoOnException.class)
	public void testHoOnQueryByteArrayTooLong() throws HoOnException, IOException {
		new HoOnResponse(encodingTooLong);
	}
	
	/**
	 * Test getErrorCode
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testGetErrorCode() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		assertTrue(ErrorCode.NOERROR.equals(r.getErrorCode()));
	}
    
	/**
	 * Test constructor with a byte array parameter
	 * @throws HoOnException if deserialization fail
	 * @throws IOException if I/O exception
	 */
	@Test
	public void testHoOnResponseByteArray() throws HoOnException, IOException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(encodingResponse);
		assertTrue(r.getErrorCode() == ErrorCode.NOERROR && r.getQueryId() == 1234 && r.getPosts().equals(posts));
	}
    
	/**
	 * Test constructor with a ErrorCode, a long, and a list of String parameters
	 */
	@Test
	public void testHoOnResponseErrorCodeLongListOfString() {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		assertTrue(r.getErrorCode() == ErrorCode.NOERROR && r.getQueryId() == 234 && r.getPosts().equals(posts));
	}
	
	/**
	 * Test constructor with a ErrorCode, a long, and a list of String parameters
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnResponseErrorCodeLongListOfStringInvalidID() {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		new HoOnResponse(ErrorCode.NOERROR, HoOnMessage.MAX_ID + 1, posts);
	}
	
	/**
	 * Test constructor with a ErrorCode, a long, and a list of String parameters
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnResponseErrorCodeLongListOfStringInvalidErrorCode() {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		new HoOnResponse(ErrorCode.NOERROR, HoOnMessage.MAX_ID + 1, posts);
	}
	
	/**
	 * Test constructor with a ErrorCode, a long, and a list of String parameters
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnResponseErrorCodeLongListOfStringInvalidPosts() {
		List<String> posts = new ArrayList<String>();
		for(int i = 0; i <= HoOnMessage.MAX_POST_NUMBER; ++i){
			posts.add("abc");
		}
		new HoOnResponse(ErrorCode.NOERROR, 1234, posts);
	}
	
	/**
	 * Test constructor with a ErrorCode, a long, and a list of String parameters
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnResponseErrorCodeLongListOfStringNullPosts() {
		new HoOnResponse(null, 1234, null);
	}
    
	/**
	 * Test constructor with a ErrorCode, a long, and a list of String parameters
	 * @throws UnsupportedEncodingException if fail to convert to String
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHoOnResponseErrorCodeLongListOfStringStringTooLong() throws UnsupportedEncodingException {
		List<String> posts = new ArrayList<String>();

	    byte[] b = new byte[HoOnMessage.MAX_POST_LENGTH + 1];
	    for(int i = 0; i < b.length; ++i){
	    	b[i] = 'x';
	    }
		
	    posts.add(new String(b, "UTF-8"));
		new HoOnResponse(ErrorCode.NOERROR, 234, posts);
	}
	
	/**
	 * Test toString method
	 * @throws HoOnException if deserialization fail
	 */
	@Test
	public void testToString() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 1234, posts);
		String s = "[error code]: " + ErrorCode.NOERROR.getErrorMessage() + ", [query ID]: 1234" + "\n[posts]:\n[0] abc\n[1] 1234\n";
		assertEquals(s, r.toString());
	}
    
	/**
	 * Test getPosts method
	 * @throws HoOnException if deserialization fail
	 */
	@Test
	public void testGetPosts() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		assertTrue(posts.equals(r.getPosts()));
	}
    
	/**
	 * Test setPosts method
	 * @throws HoOnException if deserialization fail
	 */
	@Test
	public void testSetPosts() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		List<String> posts2 = new ArrayList<String>();
		posts.add("abcde");
		posts.add("1234567");
		r.setPosts(posts2);
		assertTrue(posts2.equals(r.getPosts()));
	}
	
	/**
	 * Test setPosts method
	 * @throws HoOnException if deserialization fail
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetPostsNull() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		r.setPosts(null);
	}
	
	/**
	 * Test setPosts method
	 * @throws HoOnException if deserialization fail
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetPostsTooLong() throws HoOnException {
		List<String> posts1 = new ArrayList<String>();
		List<String> posts2 = new ArrayList<String>();
		for(int i = 0; i <= HoOnMessage.MAX_POST_NUMBER; ++i){
			posts2.add("abc");
		}
		
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts1);
		r.setPosts(posts2);
	}
	
	/**
	 * Test setPosts method
	 * @throws HoOnException if deserialization fail
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetPostsNullString() throws HoOnException {
		List<String> posts1 = new ArrayList<String>();
		List<String> posts2 = new ArrayList<String>();
		posts2.add(null);
		
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts1);
		r.setPosts(posts2);
	}
	
	/**
	 * Test setPosts method
	 * @throws HoOnException if deserialization fail
	 * @throws UnsupportedEncodingException if fail to convert to String
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetPostsStringTooLong() throws HoOnException, UnsupportedEncodingException {
		List<String> posts1 = new ArrayList<String>();
		List<String> posts2 = new ArrayList<String>();

        byte[] b = new byte[HoOnMessage.MAX_POST_LENGTH + 1];
        for(int i = 0; i < b.length; ++i){
        	b[i] = 'x';
        }
		
        posts2.add(new String(b, "UTF-8"));
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts1);
		r.setPosts(posts2);
	}
    
    
	/**
	 * Test SetErrorCode with an ErrorCode parameter
	 * @throws HoOnException if deserialization fail
	 */
	@Test
	public void testSetErrorCodeErrorCode() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		r.setErrorCode(ErrorCode.BADVERSION);
		assertTrue(ErrorCode.BADVERSION.equals(r.getErrorCode()));
	}
    
	/**
	 * Test setErrorCode with an integer parameter
	 * @throws HoOnException if deserialization fail
	 */
	@Test
	public void testSetErrorCodeInt() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		r.setErrorCode(1);
		assertTrue(ErrorCode.BADVERSION.equals(r.getErrorCode()));
	}
    
	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testEqualsObject() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		HoOnResponse r2 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		
		assertTrue(r1.equals(r2));
	}
	
	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testEqualsObjectSelf() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		
		assertTrue(r1.equals(r1));
	}
	
	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testNotEqualsObjectNull() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		
		assertFalse(r.equals(null));
	}
    

	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testNotEqualsObjectDifferentError() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		HoOnResponse r2 = new HoOnResponse(ErrorCode.NETWORKERROR, 234, posts);
		
		assertFalse(r1.equals(r2));
	}
	
	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testNotEqualsObjectDifferentID() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		HoOnResponse r2 = new HoOnResponse(ErrorCode.NOERROR, 1234, posts);
		
		assertFalse(r1.equals(r2));
	}
	
	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testNotEqualsObjectDifferentPosts() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		
		List<String> posts2 = new ArrayList<String>();
		posts.add("abcd");
		posts.add("12345");
		HoOnResponse r2 = new HoOnResponse(ErrorCode.NOERROR, 234, posts2);
		
		assertFalse(r1.equals(r2));
	}
	
	/**
	 * Test equals method
	 * @throws HoOnException if deserialization failure
	 */
	@Test
	public void testNotEqualsObjectDifferentObject() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r1 = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		
		String s = "Not a response";
		
		assertFalse(r1.equals(s));
	}
}

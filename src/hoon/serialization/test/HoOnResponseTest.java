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
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hoon.serialization.ErrorCode;
import hoon.serialization.HoOnException;
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
	private byte[] encoding;
	
	/**
	 * prepare a encoding package
	 */
	@Before
	public void prepare() {
		ByteBuffer in = MappedByteBuffer.allocate(19);
		in.put((byte) 40);
		in.put((byte) 0); // no error
		in.putInt(234);
		in.putShort((short) 2);
		in.putShort((short) 3);
		//String[] posts = {"abc", "1234"};
		in = ByteBuffer.wrap("abc".getBytes(), in.arrayOffset(), 3);
		in.putShort((short) 4);
		in = ByteBuffer.wrap("1234".getBytes(), in.arrayOffset(), 4);
		encoding = in.array();
		System.out.println(encoding);
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
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		assertTrue(encoding.equals(r.encode()));
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
		HoOnResponse r = new HoOnResponse(encoding);
		assertTrue(r.getErrorCode() == ErrorCode.NOERROR && r.getQueryId() == 234 && r.getPosts().equals(posts));
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
	 * Test toString method
	 * @throws HoOnException if deserialization fail
	 */
	@Test
	public void testToString() throws HoOnException {
		List<String> posts = new ArrayList<String>();
		posts.add("abc");
		posts.add("1234");
		HoOnResponse r = new HoOnResponse(ErrorCode.NOERROR, 234, posts);
		String s = "[error code]: " + ErrorCode.NOERROR.getErrorMessage() + ", [query ID]: 234" + "\n[posts]:abc\n1234\n";
		assertTrue(s.equals(r.toString()));
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

}

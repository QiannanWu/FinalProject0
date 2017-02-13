/************************************************
*
* Author: Dr. Donahoo
* Assignment: Program 1
* Class: CSI4321
*
************************************************/
package instayak.serialization;

/**
 * Compute MD5 hash
 * 
 * @version 1.0 30 January 2017
 * @author Dr. Donahoo
 */
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ComputeHash {
	/**
	 * constant protocol information
	 */
	private static final String PROTOCOL = "ISO8859-1";
	
	/**
	 * constant encoding information
	 */
	public static final String ENCODING = "MD5";
	
	
	
	/**
	 * Compute MD5 hash for the give String
	 * 
	 * @param msg String message
	 * 
	 * @return MD5 hash of the String
	 * @throws UnsupportedEncodingException if encoding fails
	 */
    public static String computeHash(String msg)
            throws UnsupportedEncodingException {
        try {
            MessageDigest md5 = MessageDigest.getInstance(ENCODING);
            byte[] buf = md5.digest(msg.getBytes(PROTOCOL));
            return hashToString(buf);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to get MD5", e);
        }
    }
    
    /**
     * Compute byte array to hash String
     * 
     * @param bytes the byte array needs to be convert to hash String
     * @return the hash String
     */
    public static String hashToString(byte[] bytes) {
        String hexHash = "";
        for (byte b : bytes) {
            String v = Integer.toHexString(Integer.valueOf(b & 0xff));
            if (v.length() == 1)
                v = "0" + v;
            hexHash += v.toUpperCase();
        }

        return hexHash;
    }
}


/************************************************
*
* Author: Qiannan Wu
* Assignment: Program 0
* Class: CSI4321
*
************************************************/
package instayak.serialization;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Represents a InstaYakUOn message and provides serialization/deserialization. 
 * 
 * @version 1.0 31 January 2017
 * @author Qiannan Wu
 */
public class InstaYakUOn extends InstaYakMessage{
	/**
	 * the image of the UOn message
	 */
    private byte[] image;
    
    /**
     * the category of the UOn message
     */
    private String category;
    
    /**
	 * constant operation
	 */
	public static final String OPERATION = "UOn";
    
	
	/**
	 * Constructs UOn message using set values
	 * 
	 * @param category category in UOn
	 * @param image image in UOn
	 * @throws InstaYakException if validation of category fails or image is null
	 */
	public InstaYakUOn(String category, byte[] image) throws InstaYakException{
		if(image == null){
			throw new InstaYakException("image cannot be null");
		}
		
		if(isValidCategory(category)){
			this.category = category;
			this.image = image;
		}
	}
	
	/**
	 * Check if a String is a valid category
	 * 
	 * @param category the category String needs to be check
	 * @return true if the category is valid
	 * @throws InstaYakException if the category is not valid
	 */
	public static boolean isValidCategory(String category) throws InstaYakException{
		if(category == null){
			throw new InstaYakException("The category is null");
		}
		
		if( category.length() < 1 ){
			throw new InstaYakException("The category has invalid length");
		}
		
		for(int i = 0; i < category.length(); ++i){
			if(!Character.isAlphabetic(category.charAt(i)) && !Character.isDigit(category.charAt(i))){
				throw new InstaYakException("Invalid character in category");
			}
		}
		
		return true;
	}
	
	/**
	 * Constructs new UOn message using deserialization. Only parses material specific to this message
	 * 
	 * @param in deserialization input source
	 * @throws InstaYakException if parse or validation failure
	 * @throws IOException if I/O problem
	 */
	public InstaYakUOn(MessageInput in) throws InstaYakException, IOException{
        InstaYakMessage msg = InstaYakMessage.decode(in);
		
		if(msg.getOperation() != OPERATION){
			throw new InstaYakException("Wrong operation");
		}
		
		category = ((InstaYakUOn)msg).getCategory();
		image = ((InstaYakUOn)msg).getImage();
	}
	
	/**
	 * Returns a String representation ("UOn: Category=Movie Image=500 bytes") return string representation
	 * 
	 * @return a String representation
	 */
	@Override
	public String toString(){
		return "UOn: Category=" + category + " Image=" + image.length + " bytes";
	}
	
	/**
	 * Returns category
	 * 
	 * @return category of the UOn message
	 */
	public String getCategory(){
		return category;
	}
	
	/**
	 * Returns image
	 * 
	 * @return image of the UOn message
	 */
	public byte[] getImage(){
		return image;
	}
	
	/**
	 * Sets category
	 * @param category new category
	 * @throws InstaYakException if null or invalid category
	 */
	public final void setCategory(String category) throws InstaYakException{
		if(isValidCategory(category)){
			this.category = category;
		}
	}
	
	/**
	 * Sets image
	 * @param image new image
	 * @throws InstaYakException if null image
	 */
	public final void setImage(byte[] image) throws InstaYakException{
		if(image == null){
			throw new InstaYakException("image cannot be null");
		}
		
		this.image = image.clone();
	}
	
	/**
	 * Returns message operation
	 * 
	 * @return message operation
	 */
	@Override
	public String getOperation() {
		return OPERATION;
	}
	
	/**
	 * Serializes message to given output sink
	 * 
	 * @param out serialization output sink
	 * 
	 * @throws IOException if I/O problem
	 */
	@Override
	public void encode(MessageOutput out) throws IOException {
		String s = OPERATION + " " + category + " " + Base64.getEncoder().withoutPadding().encodeToString(image) + "\r\n";
		byte[] encoding = s.getBytes(PROTOCOL);
		out.write(encoding);
	}

	/**
	 * hashCode for InstaYakUOn
	 * 
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + Arrays.hashCode(image);
		return result;
	}

	/**
	 * Override equals methods for InstaYakUOn class
	 * 
	 * @param obj the object needs to be compared
	 * 
	 * @return true/false if they have the same hash; otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstaYakUOn other = (InstaYakUOn) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (!Arrays.equals(image, other.image))
			return false;
		return true;
	}
}

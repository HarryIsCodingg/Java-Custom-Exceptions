
/**
 * 
 * @author HARINDER PARTAP SINGH
 *
 */
public class AttributeMissingException extends Exception {
	/**
	 * Creates object of DataMissingException
	 * 
	 * Class constructor 
	 */
	public AttributeMissingException(){
		super("Error: In file ,Missing Attribute. File is not converted to HTML.");
	}
	
	/**
	 * Creates object of the AttributeMissingException with user defined message.
	 * 
	 * @param message The user defined message for the exception
	 */
	public AttributeMissingException(String message){
		super(message);
	}
}

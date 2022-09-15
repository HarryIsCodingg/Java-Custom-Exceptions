
/**
 * @author HARINDER PARTAP SINGH
 *
 */

public class DataMissingException extends Exception {
	/**
	 * Creates object of DataMissingException
	 * 
	 * Class constructor 
	 */
	public DataMissingException(){
		super("Error: Input row cannot be parsed due to missing information.");
	}
	
	/**
	 * Creates object of the AttributeMissingException with user defined message.
	 * 
	 * @param message The user defined message for the exception
	 */
	public DataMissingException(String message){
		super(message);
	}
}

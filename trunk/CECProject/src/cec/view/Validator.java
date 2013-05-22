package cec.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Auxiliary Class used in the Presentation Layer that implements methods
 * to validate user's input on Email fields and Folder names.
 * */

public class Validator {
		
	Pattern pattern;
	Matcher matcher;
	
	
	/**
	 * Method to verify if the email addresses supplied by the user are valid.
	 * This takes into consideration that the user can supply both to and cc, or only to, or only cc
	 * 
	 * @param to the email address in the to field
	 * @param cc the email address in the cc field
	 * @return boolean returns true if the email address are valid 
	 */
	public boolean isValidSendees(String to, String cc) {
		if (to.isEmpty() && cc.isEmpty())
			return false;
		
		if (!to.isEmpty() && !cc.isEmpty())
			return isValidTo(to) && isValidCC(cc);
		
		if (!to.isEmpty())
			return isValidTo(to);
		
		if (!cc.isEmpty()) 
			return isValidCC(cc);
		
		return true;
	}

	private boolean isValidTo(String emailString) {
		String[] emails = emailString.split(";");
		
		pattern = Pattern.compile("(.+)@(.+)(\\.)(.+)"); 
		
		for (String email: emails ) {
			matcher = pattern.matcher(email);	
			if (matcher.find() == false)
				return false;
		}
		
		return true;
	}
	
	private boolean isValidCC(String emailString) {
		return isValidTo(emailString);
	}

	
	/**
	 * Verify that the user entered folder name does not contain any characters which 
	 * prevents it from being saved in the windows file system
	 * 
	 * @param folderName the name which was supplied by the user
	 * @return boolean returns true if the string does not contain any special characters
	 */
	public boolean isValidFolderName(String folderName) {
		pattern = Pattern.compile("^[a-zA-Z0-9_\\s]+$");  
		matcher = pattern.matcher(folderName);
		if (matcher.find() == false) {
			return false;
		}
		return true;
	}

}

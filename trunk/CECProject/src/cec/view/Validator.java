package cec.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	// right now only the view layer is using this class. consider moving 
	// in case the functionality is extended
	
	Pattern pattern;
	Matcher matcher;
	
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
		
		// use the basic one that Troy gave us, slightly enhanced!
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

	public boolean isValidFolderName(String folderName) {
		pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
		matcher = pattern.matcher(folderName);
		if (matcher.find() == false) {
			return false;
		}
		return true;
	}

}

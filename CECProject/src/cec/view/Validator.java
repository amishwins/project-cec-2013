package cec.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	
	Pattern pattern;
	Matcher matcher;

	public boolean isValidTo(String emailString) {
		String[] emails = emailString.split(";");
		pattern = Pattern.compile("(.*)@(.*)");
		
		for (String email: emails ) {
			matcher = pattern.matcher(email);	
			if (matcher.find() == false)
				return false;
		}
		
		return true;
	}
	
	public boolean isValidCC(String emailString) {
		return isValidTo(emailString);
	}

	public boolean isValidFolderName(String folderName) {
		pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
		matcher = pattern.matcher(folderName);
		if (matcher.find() == false) {
			return false;
		}
		System.out.println(matcher.group());
		return true;
	}

}

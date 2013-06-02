package cec.model;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * SearchImpl is the concrete implementation of the Search interface.
 * It has the methods to search for a specific text inside a given text.
 * 
 * It considers only letters from A-Z, numbers and the character "@".
 * It checks every word separately and return true if at least one matches.
 * Special characters are removed and words concatenated with them are treated separately.
 * 
 */

public class SearchImpl implements Search {

	String source;
	String patternToFind;
	
	public SearchImpl(String source, String patternToFind)
	{		
		
		this.patternToFind=modifiedString(patternToFind.trim().toUpperCase());
		this.source=modifiedString(source.trim().toUpperCase());	
	}
	
	
	private String modifiedString(String originalString)
	{
		String modifiedString=originalString;	
		String toRemove="[[^A-Z]&&[^0-9]&&[^@]]";
		Pattern pattern = Pattern.compile(toRemove);
		Matcher matcher = pattern.matcher(originalString);		
		
		if(matcher.find())	
			modifiedString=matcher.replaceAll(" ");	
	
		return modifiedString;			
	}
		
	
	public boolean isMatch()
	{		
		if(patternToFind.isEmpty())
			return false;
		
		String[] patternArrayToFind = patternToFind.split(" ");
		for(String eachpattern:patternArrayToFind) 
		{
			if(!eachpattern.trim().isEmpty())
			{
				Pattern pattern = Pattern.compile(eachpattern);				
				Matcher matcher = pattern.matcher(source);
				
				if(matcher.find())
					return true;				
			}			
		}		
		return false;			
	}
	
}

package cec.model;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Search {

	String source;
	String patternToFind;
	
	public Search(String source, String patternToFind)
	{		
		this.patternToFind=modifiedString(patternToFind.toUpperCase());
		this.source=modifiedString(source.toUpperCase());
	}
	
	
	private String modifiedString(String originalString)
	{
		String modifiedString=originalString;
		
		String toRemove="[[^A-Z]&&[^0-9]&&[^@]&&[^-]&&[^_]]";
		Pattern pattern = Pattern.compile(toRemove);
		Matcher matcher = pattern.matcher(originalString);//.toUpperCase());
		
		if(matcher.find())				
			modifiedString=matcher.replaceAll("");	
		
		return modifiedString;	
		
	}
	
	
	
	public boolean isMatch()
	{		
		Pattern pattern = Pattern.compile(patternToFind);
		Matcher matcher = pattern.matcher(source);
		return matcher.find();		
	}
	
}

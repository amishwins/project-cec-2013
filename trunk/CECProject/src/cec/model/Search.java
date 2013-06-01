package cec.model;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class Search {

	String source;
	String patternToFind;
	
	Search(String source, String patternToFind)
	{
		this.patternToFind = patternToFind;
		this.source = source;
	}
	
	private boolean isMatch()
	{
		Pattern pattern = Pattern.compile(patternToFind);
		Matcher matcher = pattern.matcher(source);
		return matcher.find();		
	}
	public boolean isEmailStringMatch()
	{
		return isMatch();		
	}
	
}

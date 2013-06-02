package cec.model;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;

public class SearchImpl implements Search {

	String source;
	String patternToFind;
	
	public SearchImpl(String source, String patternToFind)
	{		
		//source = "Email Content - This is the content of an email";
		//patternToFind = "This.+?content";
		
		
		this.patternToFind=modifiedString(patternToFind.trim().toUpperCase());
		this.source=modifiedString(source.trim().toUpperCase());
		
		//this.patternToFind ="{";
		//this.patternToFind = patternToFind.trim().toUpperCase();
		//this.source = source.trim().toUpperCase();
	}
	
	
	private String modifiedString(String originalString)
	{
		String modifiedString=originalString;
		
		//String toRemove="[[^A-Z]&&[^0-9]&&[^@]&&[^-]&&[^_]&&\\^p{Punct}]";
		String toRemove="[[^A-Z]&&[^0-9]&&[^@]&&[^-]&&[^_]]";
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
			//JOptionPane.showMessageDialog(null,	eachpattern);
			if(!eachpattern.trim().isEmpty())
			{
				/*JOptionPane.showMessageDialog(null,	"pattern "+eachpattern);
				JOptionPane.showMessageDialog(null,	"source "+source);*/
				Pattern pattern = Pattern.compile(eachpattern);
				
				//JOptionPane.showMessageDialog(null,	"patterncompiled "+pattern);
				Matcher matcher = pattern.matcher(source);
				
				//JOptionPane.showMessageDialog(null,	"matcher "+matcher);
				if(matcher.find())
					return true;
				
			}			
		}
		JOptionPane.showMessageDialog(null,	"End For ");
		return false;
		
		//Pattern pattern = Pattern.compile(patternToFind);
		//Matcher matcher = pattern.matcher(source);			
		/*
		if(matcher.find()==true)
			JOptionPane.showMessageDialog(null,	"OK");
			
		return matcher.find();		*/
	}
	
}

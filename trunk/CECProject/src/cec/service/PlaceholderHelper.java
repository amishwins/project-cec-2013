package cec.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderHelper {
	Pattern placeHolderRegex;
	Matcher placeHolderMatcher;
	
	public PlaceholderHelper(String text) {
		placeHolderRegex = Pattern.compile("\\$\\{.*?\\}");	
		placeHolderMatcher = placeHolderRegex.matcher(text);
		placeHolderMatcher.find();
	}
	
	public Map<Integer, Integer> positionsOfAllPlaceHolders() {
		placeHolderMatcher.reset();
		TreeMap<Integer,Integer> startAndEnd = new TreeMap<>();
		while(placeHolderMatcher.find()) {
			startAndEnd.put(getStartPositionOfNextMatch(), getEndPositionOfNextMatch());
			
		}
		return startAndEnd;
	}

	public Integer getStartPositionOfNextMatch() {
		return placeHolderMatcher.start();
	}
	
	public Integer getEndPositionOfNextMatch() {
		return placeHolderMatcher.end();
	}
	
	public void findNext() {
		placeHolderMatcher.find();
	}
	
}

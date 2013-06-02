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
		Integer startPosition;
		try {
			startPosition = placeHolderMatcher.start();
		} catch (IllegalStateException e) {
			startPosition = -1;
		}
		return startPosition;
	}
	
	public Integer getEndPositionOfNextMatch() {
		Integer endPosition;
		try {
			return placeHolderMatcher.end();
		} catch (IllegalStateException e) {
			endPosition = -1;
		}
		return endPosition;
	}
	
	public boolean findNext() {
		return placeHolderMatcher.find();
	}

	public void reset() {
		placeHolderMatcher.reset();
		
	}
	
}

package cec.model;

/**
 * The Interface Searchable exposes the method responsible  
 * for searching in an email 
 */
public interface Searchable {

	
	public boolean isMatch(String pattern);
}


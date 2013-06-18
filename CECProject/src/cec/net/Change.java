package cec.net;

import java.io.Serializable;

/**
 * Class that represents a Change object - the lowest level of granularity in a Meeting Change Set.<BR>
 * Stores the Meeting field that has been changed and its values before and after. 
 * These values are compared to verify if Client and Server are properly synchronized.
 */

public class Change implements Serializable {
	private static final long serialVersionUID = 1L;
	public ChangeSetFields field;
	public String before;
	public String after;
	
	public Change() {
		
	}
	
	public Change(ChangeSetFields field, String before, String after) {
		this.field = field;
		this.before = before;
		this.after = after;
	}
	
	public boolean isEqualTo(Change c) {
		return (c.field.equals(field) && c.before.equals(before) && c.after.equals(after));
	}
}
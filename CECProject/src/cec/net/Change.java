package cec.net;

import java.io.Serializable;

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
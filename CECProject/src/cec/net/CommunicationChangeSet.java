package cec.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class CommunicationChangeSet implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final ChangeSetState state; 
	private final UUID meetingID;
	private ArrayList<Change> changes;
	
	public CommunicationChangeSet(ChangeSetState state, UUID meetingID) {
		this.state = state;
		this.meetingID = meetingID;
		changes = new ArrayList<Change>();
	}
	
	public ChangeSetState getState() {
		return state;
	}
	
	public boolean isAccept() {
		return (getState() == ChangeSetState.ACCEPT);
	}
	
	public boolean isDecline() {
		return (getState() == ChangeSetState.DECLINE);
	}
	
	public boolean isChange() {
		return (getState() == ChangeSetState.CHANGE);
	}
	
	public UUID getId() {
		return meetingID;
	}
	
	public void addChanges() {
		if (state != ChangeSetState.CHANGE) {
			throw new IllegalStateException("Cannot add changes to this change set of type: " + state);
		}
	}

	public void send() {
		NetworkHelper nh = NetworkHelper.getReference();
		nh.sendChangeSet(this);		
	}

	public void addChange(final ChangeSetFields body, final String inBefore, final String inAfter) {
		
		// TODO: should we just use some sort of set here? I don't know!!
		for(Change c: changes) {
			if (c.field.equals(body)) {
				// should only add changes once
				throw new IllegalStateException("Cannot have more than one change for a given field: " + body);
			}
		}
		
		Change c = new Change(); 
		c.field = body; 
		c.before = inBefore; 
		c.after = inAfter;
		
		changes.add(c);
		
	}

	public ArrayList<Change> getChanges() {
		// defensive copy
		return new ArrayList<Change>(changes);
	}
}

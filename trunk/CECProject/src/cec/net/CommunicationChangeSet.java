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

	public boolean isChangeAccepted() {
		return (getState() == ChangeSetState.CHANGE_ACCEPTED);
	}
	
	public boolean isChangeRejected() {
		return (getState() == ChangeSetState.CHANGE_REJECTED);
	}
	
	public UUID getId() {
		return meetingID;
	}
	

	public void send() {
		NetworkHelper nh = NetworkHelper.getReference();
		nh.sendChangeSet(this);		
	}
	
	public void addChange(Change c) {
		addChange(c.field, c.before, c.after);
	}

	public void addChange(final ChangeSetFields body, final String inBefore, final String inAfter) {
		checkSanityOfChange(body, inBefore, inAfter);
		
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

	private void assertChangeSetState() {
		// only allow sending change sets if the type of operation is CHANGE or CHANGE_REJECTED
		if (state == ChangeSetState.ACCEPT || state == ChangeSetState.DECLINE || state == ChangeSetState.CHANGE_ACCEPTED  ) {
			throw new IllegalStateException("Cannot add changes to this change set of type: " + state);
		}
	}
	
	private void checkSanityOfChange(final ChangeSetFields body, final String inBefore, final String inAfter) {
		assertChangeSetState();

		if (body == null || inBefore == null || inAfter == null)
			throw new IllegalArgumentException("Change fields cannot be null");
		
		// TODO: should we just use some sort of set here? I don't know!!
		for(Change c: changes) {
			if (c.field.equals(body)) {
				// should only add changes once
				throw new IllegalStateException("Cannot have more than one change for a given field: " + body);
			}
		}	

		if (inBefore.equals(inAfter)) {
			throw new IllegalArgumentException("Before and after are the same!");
		}
	}


}

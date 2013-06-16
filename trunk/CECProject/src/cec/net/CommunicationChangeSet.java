package cec.net;

import java.util.UUID;

public class CommunicationChangeSet {
	
	private final ChangeSetState state; 
	private final UUID meetingID;
	
	public CommunicationChangeSet(ChangeSetState state, UUID meetingID) {
		this.state = state;
		this.meetingID = meetingID;
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
}

package cec.view;

import java.util.ArrayList;
import java.util.UUID;

import cec.net.Change;

public class MeetingViewFieldChanges {
	public UUID meetingID;
	
	public ServerStatusForMeetingChange state;
	public MeetingViewEntity valuesFromServer;
	public MeetingViewEntity userChanges;
	public ArrayList<Change> changes;
	
	public MeetingViewFieldChanges(UUID id){
		meetingID = id;
		changes = new ArrayList<>();
	}
	
	public boolean isAccepted(){
		return (state == ServerStatusForMeetingChange.ACCEPTED);
	}
	
	public boolean isRejected(){
		return (state == ServerStatusForMeetingChange.REJECTED);
	}
	
	public boolean isNoResponse(){
		return (state == ServerStatusForMeetingChange.NO_RESPONSE);
	}
	
	public boolean isNoChangesFromUser(){
		return (state == ServerStatusForMeetingChange.NO_CHANGES_FROM_USER);
	}
	
	public boolean isNotConnected(){
		return (state == ServerStatusForMeetingChange.NOT_CONNECTED);
	}
}

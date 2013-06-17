package cec.service;

import cec.net.ChangeSetFields;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;
import cec.view.MeetingViewEntity;

public class ClientMeetingMerger {
	MeetingViewEntity before;
	MeetingViewEntity after;
	
	public ClientMeetingMerger(MeetingViewEntity before, MeetingViewEntity after) {
		this.before = before;
		this.after = after;
	}
	

	public CommunicationChangeSet getChangeSet() {
		CommunicationChangeSet ccs = new CommunicationChangeSet(ChangeSetState.CHANGE, before.getId());
		ccs.addChange(ChangeSetFields.ATTENDEES, before.getAttendees(), after.getAttendees());
		ccs.addChange(ChangeSetFields.SUBJECT, before.getSubject(), after.getSubject());
		ccs.addChange(ChangeSetFields.PLACE, before.getPlace(), after.getPlace());
		ccs.addChange(ChangeSetFields.START_DATE, before.getStartDate(), after.getStartDate());
		ccs.addChange(ChangeSetFields.START_TIME, before.getStartTime(), after.getStartTime());
		ccs.addChange(ChangeSetFields.END_DATE, before.getEndDate(), after.getEndDate());
		ccs.addChange(ChangeSetFields.END_TIME, before.getEndTime(), after.getEndTime());
		ccs.addChange(ChangeSetFields.BODY, before.getBody(), after.getBody());
				
		return ccs;
	}

	public boolean wereChangesMade() {
		
		if (!before.getAttendees().equals(after.getAttendees()))
			return true;

		if (!before.getSubject().equals(after.getSubject()))
			return true;
		
		if (!before.getPlace().equals(after.getPlace()))
			return true;
		
		if (!before.getStartDate().equals(after.getStartDate()))
			return true;
		
		if (!before.getStartTime().equals(after.getStartTime()))
			return true;

		if (!before.getEndDate().equals(after.getEndDate()))
			return true;
		
		if (!before.getEndTime().equals(after.getEndTime()))
			return true;
		
		if (!before.getBody().equals(after.getBody()))
			return true;
					
		return false;

	}
	
}

package cec.service;

import cec.net.ChangeSetFields;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;
import cec.view.MeetingViewEntity;

public class ClientMeetingMerger {

	public CommunicationChangeSet getChangeSet(MeetingViewEntity before, MeetingViewEntity after) {
		CommunicationChangeSet ccs = new CommunicationChangeSet(ChangeSetState.CHANGE, before.getId());
		
		if (!before.getAttendees().equals(after.getAttendees()))
			ccs.addChange(ChangeSetFields.ATTENDEES, before.getAttendees(), after.getAttendees());

		if (!before.getSubject().equals(after.getSubject()))
			ccs.addChange(ChangeSetFields.SUBJECT, before.getSubject(), after.getSubject());
		
		if (!before.getPlace().equals(after.getPlace()))
			ccs.addChange(ChangeSetFields.PLACE, before.getPlace(), after.getPlace());
		
		if (!before.getStartDate().equals(after.getStartDate()))
			ccs.addChange(ChangeSetFields.START_DATE, before.getStartDate(), after.getStartDate());
		
		if (!before.getStartTime().equals(after.getStartTime()))
			ccs.addChange(ChangeSetFields.START_TIME, before.getStartTime(), after.getStartTime());

		
		if (!before.getEndDate().equals(after.getEndDate()))
			ccs.addChange(ChangeSetFields.END_DATE, before.getEndDate(), after.getEndDate());
		
		if (!before.getEndTime().equals(after.getEndTime()))
			ccs.addChange(ChangeSetFields.END_TIME, before.getEndTime(), after.getEndTime());
		
		if (!before.getBody().equals(after.getBody()))
			ccs.addChange(ChangeSetFields.BODY, before.getBody(), after.getBody());
					
		return ccs;

	}

}

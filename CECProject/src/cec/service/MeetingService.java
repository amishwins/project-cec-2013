package cec.service;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import cec.exceptions.UserIsNotConnectedException;
import cec.model.FolderFactory;
import cec.model.Meeting;
import cec.model.MeetingBuilder;
import cec.net.Change;
import cec.net.ChangeSetFields;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;
import cec.net.NetworkHelper;
import cec.net.ServerThreadPerClient;
import cec.view.MeetingViewEntity;
import cec.view.MeetingViewFieldChanges;
import cec.view.ServerStatusForMeetingChange;

/**
 * Expose the action that can be done on an meeting from the model layer
 */
public class MeetingService {
	
	static Logger logger = Logger.getLogger(MeetingService.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( MeetingService.class.getPackage().getName() ) );
    }	

	/**
	 * This method communicates the model layer the meetingInView in parameter
	 * received from the view layer has to be sent.
	 * PostCondition: Meeting email has been sent to the attendees.
	 * Meting has been saved into the Meetings folder.
	 * 
	 * @param meetingInView
	 *            the MeetingViewEntity object received from view layer
	 */
	public void sendMeeting(MeetingViewEntity meetingInView) {
		MeetingBuilder meetingBuilder = new MeetingBuilder();
		Meeting newMeeting = meetingBuilder
				.withId(meetingInView.getId())
				.withFrom()
				.withAttendees(meetingInView.getAttendees())
				.withStartDate(meetingInView.getStartDate())
				.withEndDate(meetingInView.getEndDate())
				.withStartTime(meetingInView.getStartTime())
				.withEndTime(meetingInView.getEndTime())
				.withPlace(meetingInView.getPlace())
				.withSubject(meetingInView.getSubject())
				.withBody(meetingInView.getBody())
				.computelastModifiedTime()
				.computeSentTime()
				.withParentFolder(
						FolderFactory.getFolder(meetingInView
								.getFolder())).build();
		newMeeting.send();
	}
	
	public void updateMeeting(MeetingViewEntity meetingInView) {
		MeetingBuilder meetingBuilder = new MeetingBuilder();
		Meeting newMeeting = meetingBuilder
				.withId(meetingInView.getId())
				.withFrom()
				.withAttendees(meetingInView.getAttendees())
				.withStartDate(meetingInView.getStartDate())
				.withEndDate(meetingInView.getEndDate())
				.withStartTime(meetingInView.getStartTime())
				.withEndTime(meetingInView.getEndTime())
				.withPlace(meetingInView.getPlace())
				.withSubject(meetingInView.getSubject())
				.withBody(meetingInView.getBody())
				.computelastModifiedTime()
				.computeSentTime()
				.withParentFolder(
						FolderFactory.getFolder(meetingInView
								.getFolder())).build();
		newMeeting.saveAfterAccept();
	}
	
	

	/**
	 * This method communicates the model layer the meetingInView object has to be
	 * deleted.
	 * Postcondition: Meeting object has been deleted from the System.
	 * Invariant: Meeting object file has not been moved during the deletion process.
	 * 
	 * @param meetingInView
	 *            the MeetingViewEntity object received from view layer
	 */
	public void delete(MeetingViewEntity meetingInView) {
		if(NetworkHelper.isConnectedToServer()) {
			// send accept email (ChangeSet)
			CommunicationChangeSet ccs = new CommunicationChangeSet(ChangeSetState.DECLINE, meetingInView.getId());
			ccs.send();
			
			// delete meeting object
			Meeting meeting = convertMeetingInViewToMeetinglModel(meetingInView);
			meeting.delete();
			}
			else {
				throw new UserIsNotConnectedException();			
			}
	}

	private Meeting convertMeetingInViewToMeetinglModel(
			MeetingViewEntity meetingInView) {
		MeetingBuilder meetingBuilder = new MeetingBuilder();

		Meeting meeting = meetingBuilder
				.withId(meetingInView.getId())
				.withFrom(meetingInView.getFrom())
				.withAttendees(meetingInView.getAttendees())
				.withStartDate(meetingInView.getStartDate())
				.withEndDate(meetingInView.getEndDate())
				.withStartTime(meetingInView.getStartTime())
				.withEndTime(meetingInView.getEndTime())
				.withPlace(meetingInView.getPlace())
				.withSubject(meetingInView.getSubject())
				.withBody(meetingInView.getBody())
				.computelastModifiedTime()
				.computeSentTime()
				.withParentFolder(
						FolderFactory.getFolder(meetingInView
								.getFolder())).build();
		return meeting;
	}

	public MeetingViewFieldChanges sendUpdate(MeetingViewEntity before, MeetingViewEntity after) {
		
		ClientMeetingMerger cm = new ClientMeetingMerger(before, after);
		CommunicationChangeSet ccs = cm.getChangeSet();
		MeetingViewFieldChanges mvf = new MeetingViewFieldChanges(ccs.getId());
		if (cm.wereChangesMade()) {
			
			CommunicationChangeSet serverResponse = NetworkHelper.getReference().sendChangeSet(ccs);

			if(serverResponse == null) {
				mvf.state = ServerStatusForMeetingChange.NOT_CONNECTED;
			}
			
			if (serverResponse.isChangeAccepted()) {
				mvf.state = ServerStatusForMeetingChange.ACCEPTED;
			}
			else if (serverResponse.isChangeRejected()) {
								
				ArrayList<Change> changes = serverResponse.getChanges();
				for(Change c: changes) {
					logger.info("Overall change: " + c.field + " " + c.before + " " + c.after);
					
					if(c.field.equals(ChangeSetFields.ATTENDEES)) {
						before.setAttendees(c.before);
						logger.info("Updated attendees");
					}
					
					if(c.field.equals(ChangeSetFields.BODY)) {
						before.setBody(c.before);
						logger.info("Updated body");
					}
					
					if(c.field.equals(ChangeSetFields.END_DATE)) {
						before.setEndDate(c.before);
						logger.info("Updated end date");
					}
					
					if(c.field.equals(ChangeSetFields.END_TIME)) {
						before.setEndTime(c.before);
						logger.info("Updated end time");
					}
					
					if(c.field.equals(ChangeSetFields.PLACE)) {
						before.setPlace(c.before);
						logger.info("Updated end place");
					}
					
					if(c.field.equals(ChangeSetFields.START_DATE)) {
						before.setStartDate(c.before);
						logger.info("Updated start date");
					}
					
					if(c.field.equals(ChangeSetFields.START_TIME)) {
						before.setStartTime(c.before);
						logger.info("Updated start time");
					}
					
					if(c.field.equals(ChangeSetFields.SUBJECT)) {
						before.setSubject(c.before);
						logger.info("Updated subject");
					}
				}
				
				mvf.valuesFromServer = before;  // squashed
				mvf.userChanges = after;
				mvf.changes = changes;
				mvf.state = ServerStatusForMeetingChange.REJECTED;
			}
		}

		else {
			mvf.state = ServerStatusForMeetingChange.NO_CHANGES_FROM_USER;		
		}	
		return mvf;
		
	}

	public MeetingViewEntity merge(MeetingViewFieldChanges meetingViewChanges) {
		MeetingViewEntity squashed = meetingViewChanges.valuesFromServer; 
		MeetingViewEntity result = meetingViewChanges.userChanges;
		boolean datesChanged = false;

		for(Change c: meetingViewChanges.changes) {
			
			if(c.field.equals(ChangeSetFields.ATTENDEES)) {
				result.setAttendees(">>>From server: " + squashed.getAttendees() + " <<<Your version: " + result.getAttendees());
			}
			
			// For body, we add a new line!!
			if(c.field.equals(ChangeSetFields.BODY)) {
				result.setBody(">>>From server: " + squashed.getBody() + "\n\n<<<Your version: " + result.getBody());
			}
			
			if(c.field.equals(ChangeSetFields.PLACE)){
				result.setPlace(">>>From server: " + squashed.getPlace() +" <<<Your version: " + result.getPlace());
			}

			if(c.field.equals(ChangeSetFields.SUBJECT)){
				result.setSubject(">>>From server: " + squashed.getSubject() + " <<<Your version: " + result.getSubject());
			}
			
			if(c.field.equals(ChangeSetFields.END_DATE)){
				result.setEndDate(squashed.getEndDate());
				datesChanged = true;
			}
			if(c.field.equals(ChangeSetFields.START_DATE)){
				result.setStartDate(squashed.getStartDate());
				datesChanged = true; 
			}
			if(c.field.equals(ChangeSetFields.START_TIME)){
				result.setStartTime(squashed.getStartTime());
				datesChanged = true; 
			}			
			if(c.field.equals(ChangeSetFields.END_TIME)){
				result.setEndTime(squashed.getEndTime());
				datesChanged = true;
			}

		}
		if (datesChanged)
			JOptionPane.showMessageDialog(null, "Be careful, some dates were changed from the server!");
		
		return result;
	}

}

package cec.service;

import javax.swing.JOptionPane;

import cec.exceptions.UserIsNotConnectedException;
import cec.model.Meeting;
import cec.model.MeetingBuilder;
import cec.model.FolderFactory;
import cec.net.ChangeSetState;
import cec.net.CommunicationChangeSet;
import cec.net.NetworkHelper;
import cec.view.MeetingViewEntity;

/**
 * Expose the action that can be done on an meeting from the model layer
 */
public class MeetingService {

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

	public void sendUpdate(MeetingViewEntity before, MeetingViewEntity after) {
		
		ClientMeetingMerger cm = new ClientMeetingMerger();
		CommunicationChangeSet ccs = cm.getChangeSet(before, after);
		if (ccs.getChanges().size() == 0) {
			// TODO: THIS IS THE WRONG PLACE TO DO THIS
			JOptionPane.showMessageDialog(null, "No changes were made");
		}
		else {
			NetworkHelper.getReference().sendChangeSet(ccs);
		}
		
	}

}

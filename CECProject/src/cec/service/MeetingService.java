package cec.service;

import cec.model.Meeting;
import cec.model.MeetingBuilder;
import cec.model.FolderFactory;
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
		Meeting meeting = convertMeetingInViewToMeetinglModel(meetingInView);
		meeting.delete();
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

}

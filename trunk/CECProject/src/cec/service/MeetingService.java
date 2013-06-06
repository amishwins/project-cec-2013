package cec.service;

import cec.model.Meeting;
import cec.model.MeetingBuilder;
import cec.model.FolderFactory;
import cec.view.MeetingViewEntity;

/**
 * Expose the action that can be done on an email from the model layer
 */
public class MeetingService {

	/**
	 * This method communicates the model layer the emailInView in parameter
	 * received from the view layer has to be sent
	 * 
	 * @param meetingInView
	 *            the EmailViewEntity object received from view layer
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
	 * This method communicates the model layer the emailInView object has to be
	 * deleted and has to set in the draft folder
	 * 
	 * @param meetingInView
	 *            the EmailViewEntity object received from view layer
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
		return meeting;
	}

}

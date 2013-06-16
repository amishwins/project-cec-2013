/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cec.exceptions.CannotDeleteSystemFolderException;
import cec.exceptions.RootFolderSubfolderCreationException;


/**
 * Folder model object. A runtime folder object represents a physical folder
 * from the file system
 * 
 */
public class MeetingsFolder extends Folder {

	public MeetingsFolder(String path) {
		super(path);
    }

	/**
	 * @return the collection of meetings which are currently in this folder
	 */
	@Override
	public Iterable<Meeting> loadMeetings() {
		List<Meeting> meetingsInFolder = new LinkedList<Meeting>();
		Iterable<Map<String, String>> meetingsData = folderDao.loadMeetings(getPath());

		for (Map<String, String> meetingData : meetingsData) {
			MeetingBuilder meetingBuilder = new MeetingBuilder();
			Meeting meeting = meetingBuilder
					.withId(UUID.fromString(meetingData.get("Id")))
					.withFrom(meetingData.get("From"))
					.withAttendees(meetingData.get("Attendees"))
					.withStartDate(meetingData.get("MeetingStartDate"))
					.withEndDate(meetingData.get("MeetingEndDate"))
					.withStartTime(meetingData.get("MeetingStartTime"))
					.withEndTime(meetingData.get("MeetingEndTime"))
					.withPlace(meetingData.get("MeetingPlace"))
					.withSubject(meetingData.get("Subject"))
					.withBody(meetingData.get("Body"))
					.withLastModifiedTime(meetingData.get("LastModifiedTime"))
					.withSentTime(meetingData.get("SentTime"))
					.withParentFolder(
							FolderFactory.getFolder(meetingData
									.get("ParentFolder"))).build();
			meetingsInFolder.add(meeting);
		}
		Collections.sort(meetingsInFolder);
		return meetingsInFolder;
	}

	@Override
	public Iterable<Email> loadEmails() {
		throw new UnsupportedOperationException();
	}
	@Override
	public Iterable<Email> searchEmails(String searchString, Folder folderToSearchIn) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void createSubFolder(String newFolderName) {
		throw new RootFolderSubfolderCreationException();
	}

	@Override
	public void delete() {
		handleSystemDelete();
	}

	protected void handleSystemDelete() {
		throw new CannotDeleteSystemFolderException();
	}

	@Override
	public Iterable<Email> loadRuleApplicableEmails() {
		throw new RuntimeException("Cannot call this method from the meeting folder");
	}
	
	
}

package cec.persistence;

import java.util.Map;
import java.util.UUID;

/**
 * The Interface MeetingDao exposes the lower level methods to the Model layer. 
 * 
 */
public interface MeetingDao {
	
	/**
	 * Saves each meeting object to its equivalent lower level representation( for example : a File in this case.)
	 *
	 * @param id the id
	 * @param from the from
	 * @param attendees the attendees
	 * @param meetingStartDate the meeting start date
	 * @param meetingEndDate the meeting end date
	 * @param meetingStartTime the meeting start time
	 * @param meetingEndTime the meeting end time
	 * @param meetingPlace the meeting place
	 * @param subject the subject
	 * @param body the body
	 * @param lastModifiedTime the last modified time
	 * @param sentTime the sent time
	 * @param location the location
	 */
	public void save(UUID id, String from, String attendees,
			String meetingStartDate, String meetingEndDate,
			String meetingStartTime, String meetingEndTime,
			String meetingPlace, String subject, String body,
			String lastModifiedTime, String sentTime, String location);
	
	/**
	 * Delete each email meeting from the persistence layer.
	 *
	 * @param path the path
	 * @param fileName the file name
	 */
	public void delete(String path, UUID fileName);
	
	
	/**
	 * Loads an equivalent lower level representation of an meeting from a specific folder.
	 * It basically loads the email field values and returns a Map of
	 * those values.
	 *
	 * @param folder the folder
	 * @param FileName the file name
	 * @return the map
	 */
	public Map<String, String> loadMeeting(String folder, String FileName);
}

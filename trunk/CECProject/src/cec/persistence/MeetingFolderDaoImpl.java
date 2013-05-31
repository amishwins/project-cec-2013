package cec.persistence;

import java.util.*;
import java.util.logging.Logger;
import java.io.File;
import org.apache.commons.io.filefilter.FileFileFilter;


/**
 * 
 * FolderDaoImpl class provides the concrete implementation for the life-cycle 
 * methods for the folder domain object.
 * 
 */
public class MeetingFolderDaoImpl implements MeetingFolderDao {
	
	static Logger logger = Logger.getLogger(MeetingFolderDaoImpl.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( MeetingFolderDaoImpl.class.getPackage().getName() ) );
    }

	
	/**
     * It returns the equivalent low level representation of email object under given 
     *  folder specified by the argument folder. It basically returns data pertaining 
     *  to all the emails under a given folder in the form of a collection of 
     *  Map<String,String>.
     * 
     * @param meetingFolder the folder
     * @return the iterable
     */
	public Iterable<Map<String, String>> loadMeetings(String meetingFolder) {
		Collection<Map<String, String>> listOfMeetings = new ArrayList<>();
		Map<String, String> meeting;
		MeetingDao meetingDao = new MeetingXMLDao();

		String[] xmlFilesName = getFileNames(meetingFolder);
		for (String xmlFileName : xmlFilesName) {
			meeting = meetingDao.loadMeeting(meetingFolder, xmlFileName);// read(folder,
															// xmlFileName);
			listOfMeetings.add(meeting);
		}
		return listOfMeetings;
	}

	/**
	 * It returns only the file names under a given directory specified by argument 
	 * dir.
	 *
	 * @param dir the dir
	 * @return the file names
	 */
	private String[] getFileNames(String dir) {
		File folder = new File(dir);
		String[] xmlFiles = folder.list(FileFileFilter.FILE);
		return xmlFiles;
	}
}

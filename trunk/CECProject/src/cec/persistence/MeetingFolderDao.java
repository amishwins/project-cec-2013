package cec.persistence;
import java.util.*;


/**
 * The Interface FolderDao exposes all the life-cycle methods for the folder domain object.
 *  classes implementing this class provide the concrete implementation for the 
 *  life-cycle methods. 
 */
public interface MeetingFolderDao {
    
    /**
     * It loads all meetings objects from the folder specified by argument folder.
     *
     * @param folder the folder
     * @return the iterable
     */
    public Iterable<Map<String,String>> loadMeetings(String folder);
    
}
 
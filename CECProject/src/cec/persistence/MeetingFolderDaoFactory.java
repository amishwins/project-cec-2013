package cec.persistence;

/**
 * A factory for creating MeetingFolderDao objects.
 */
public class MeetingFolderDaoFactory {
    
    /**
     * It returns the instance of class implementing 
     * the FolderDao Interface.
     *
     * @return the meeting folder dao instance
     */
    public static MeetingFolderDao getMeetingFolderDaoInstance() {
        return new MeetingFolderDaoImpl();
    }
}

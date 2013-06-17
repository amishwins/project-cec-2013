package cec.persistence;

/**
 * A factory for creating MeetingDao objects.
 */
public class MeetingDaoFactory {
    
    /**
     * It returns the instance of the class implementing the
     * MeetingDao interface.
     *
     * @return the meeting dao instance
     */
    public static MeetingDao getMeetingDaoInstance() {
        return new MeetingXMLDao();
    }
}

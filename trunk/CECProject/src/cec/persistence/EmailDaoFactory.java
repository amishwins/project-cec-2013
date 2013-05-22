package cec.persistence;

/**
 * A factory for creating EmailDao objects.
 */
public class EmailDaoFactory {
    
    /**
     * It returns the instance of the class implementing the 
     * EmailDao interface.
     *
     * @return the email dao instance
     */
    public static EmailDao getEmailDaoInstance() {
        return new EmailXMLDao();
    }
}

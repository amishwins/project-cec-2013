package cec.persistence;

/**
 * A factory for creating EmailDao objects.
 */
public class EmailTemplateDaoFactory {
    
    /**
     * It returns the instance of the class implementing the 
     * EmailTemplateDao interface.
     *
     * @return the email template DAO instance
     */
    public static EmailTemplateDao getEmailTemplateDaoInstance() {
        return new EmailTemplateXMLDao();
    }
}

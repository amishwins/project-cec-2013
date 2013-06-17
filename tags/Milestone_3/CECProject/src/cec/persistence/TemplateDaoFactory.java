package cec.persistence;

/**
 * A factory for creating EmailDao objects.
 */
public class TemplateDaoFactory {
    
    /**
     * It returns the instance of the class implementing the 
     * EmailTemplateDao interface.
     *
     * @return the email template DAO instance
     */
    public static TemplateDao getTemplateDaoInstance() {
        return new TemplateXMLDao();
    }
}

package cec.persistence;

/**
 * A factory for creating EmailDao objects.
 */
public class RuleDaoFactory {
    
    /**
     * It returns the instance of the class implementing the 
     * EmailDao interface.
     *
     * @return the email dao instance
     */
    public static RuleDao getRuleDaoInstance() {
        return new RuleXMLDao();
    }
}

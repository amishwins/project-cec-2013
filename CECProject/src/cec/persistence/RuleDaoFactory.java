package cec.persistence;

/**
 * A factory for creating RuleDao objects.
 */
public class RuleDaoFactory {
    
    /**
     * It returns the instance of the class implementing the 
     * RuleDao interface.
     *
     * @return the rule dao instance
     */
    public static RuleDao getRuleDaoInstance() {
        return new RuleXMLDao();
    }
}

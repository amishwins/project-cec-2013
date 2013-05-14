package model;

import java.util.List;

public class Hierarchy {
    private Object[] hierarchy;
    
    
    public Hierarchy() {
        // code to get the hierarchy from the persistence 
        Object[] h = 
                { "Emails",
                  "Inbox",
                  new Object[] { "Jokes", "Quotes" },
                  "Sent",
                  "Drafts" };
        hierarchy = h;
    }
    
    public Object[] getHierarchy() {
       return hierarchy;
   }    
}

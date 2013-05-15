package model;

import java.lang.reflect.Array;
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
                  "Drafts",
                  new someStrangeObject()
                };
        hierarchy = h;
    }
    
    public Object[] getHierarchy() {
       return hierarchy;
   }    
}

class someStrangeObject {
    private String title;
    
    private String path;
    
    public someStrangeObject() {
        title = "dog";
    }
  
    @Override
    public String toString() {
        return title;
    }
}
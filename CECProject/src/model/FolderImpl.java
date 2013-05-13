/**
 * @author Deyvid William / Created 07-May-13
 */

package model;

import persistence.FolderDao;
import persistence.FolderDaoFactory;
import java.util.*;


public class FolderImpl implements Folder {
    
    //Class atributes - Private Variables
    private String Name;
    private String Path;
    
   // public DefaultMutableTreeNode getFolderList(){
        //FolderStructure a = new FolderStructure();
        //return a.getStructure();
   // }
    
    public Iterable<Email> loadEmails(String folder){
        Collection<Email> emailList = new LinkedList<Email>();
        FolderDao folder1 = FolderDaoFactory.getFolderDaoInstance();
        Iterable<Map<String,String>> emailsData = folder1.loadEmails(folder);
        for(Map<String,String> emailData: emailsData){
          //System.out.println("TO"+email.get("To"));
          //System.out.println("CC"+email.get("CC"));
          //System.out.println("Subject"+email.get("Subject"));
          //System.out.println("Body"+email.get("Body"));
          //System.out.println("LastAccessedTime"+email.get("LastAccessedTime"));
          EmailBuilder emailBuilder = new EmailBuilder();
          Email email = emailBuilder.withTo(emailData.get("To"))
                                     .withCC(emailData.get("CC"))
                                     .withSubject(emailData.get("Subject"))
                                     .withBody(emailData.get("Body"))
                                     .withLastAccessedTime(emailData.get("LastAccessedTime"))
                                     .buildWithAccessedTime();
          emailList.add(email);
          
        }
        
        
        
        return emailList;
    }
     
   
    
    
    public static void main(String[] args) {
         
         //Iterable <Email> emails = loadEmails("emails/Inbox");
        // for(Email eachEmail:emails ){
        //   System.out.println("File Name : "+ eachEmail.getLastAccessedTime()); 
        //   System.out.println("To : "+ eachEmail.getTo());
        //   System.out.println("CC : "+ eachEmail.getCC());
        //   System.out.println("Subject : "+ eachEmail.getSubject());
         //  System.out.println("Body : "+ eachEmail.getBody());
         //  System.out.println("LastAccessedTime : "+ eachEmail.getLastAccessedTime());
         //}
     }
     
}

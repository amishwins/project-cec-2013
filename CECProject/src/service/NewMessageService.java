/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import model.Email;
import model.EMailBuilder;
/**
 *
 * @author Pankaj Kapania
 */
public class NewMessageService {
    
   public void  NewMessageService(){
        
    }
   
   public void sendMessage(String to, String cc, String subject, String body){
       EMailBuilder msg = new EMailBuilder();
       Email message = msg.withTo(to)
                          .withSubject(subject)
                          .withBody(body)
                          .build();
       message.saveToSentFolder();
   }
   public void draftMessage(String To, String CC, String Subject, String content){
       
   }
   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import model.Email;
import model.EmailBuilder;
/**
 *
 * @author Pankaj Kapania
 */
public class NewMessageService {
    
   public void  NewMessageService(){
        
    }
   
   public void sendEmail(String to, String cc, String subject, String body){
       EmailBuilder mailBuilder = new EmailBuilder();
       Email email = mailBuilder.withTo(to)
                          .withSubject(subject)
                          .withBody(body)
                          .withCC(cc)
                          .build();
       email.send();
   }
   public void draftEmail(String to, String cc, String subject, String body){
       EmailBuilder mailBuilder = new EmailBuilder();
       Email email = mailBuilder.withTo(to)
                          .withSubject(subject)
                          .withBody(body)
                          .withCC(cc)
                          .build();
       email.saveToDraftFolder();
   }
   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.service;
import cec.model.Email;
import cec.model.EmailBuilder;
/**
 *
 * @author Pankaj Kapania
 */
public class EmailService {
    
   public void  NewMessageService(){
        
    }
   
   public void sendEmail(String to, String cc, String subject, String body){
       EmailBuilder mailBuilder = new EmailBuilder();
       Email newEmail = mailBuilder.withTo(to)
                          .withSubject(subject)
                          .withBody(body)
                          .withCC(cc)
                          .build();
       newEmail.send();
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

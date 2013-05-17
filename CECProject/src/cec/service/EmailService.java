package cec.service;

import cec.model.Email;
import cec.model.EmailBuilder;

public class EmailService {    
      
   public void sendEmail(String from, String to, String cc, String subject, String body){
       EmailBuilder mailBuilder = new EmailBuilder();
       Email newEmail = mailBuilder.computeID()
    		                       .withFrom(from)
    		   					   .withTo(to)
                                   .withSubject(subject)
                                   .withBody(body)
                                   .withCC(cc)
                                   .computelastModifiedTime()
                                   .computeSentTime()
                                   .build();
       newEmail.send();
   }

   public void draftEmail(String from, String to, String cc, String subject, String body){
       EmailBuilder mailBuilder = new EmailBuilder();
       Email email = mailBuilder.computeID()
               					.withFrom(from)
               					.withTo(to)
               					.withSubject(subject)
               					.withBody(body)
               					.withCC(cc)
               					.computelastModifiedTime()
               					.computeSentTime()
               					.build();
       email.saveToDraftFolder();
   }   
}

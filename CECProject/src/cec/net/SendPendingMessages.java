package cec.net;

import java.io.EOFException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.model.Email;

public class SendPendingMessages implements Runnable {

	static Logger logger = Logger
			.getLogger(SendPendingMessages.class.getName());

	static {
		logger.setParent(Logger.getLogger(SendPendingMessages.class
				.getPackage().getName()));
	}

	private String emailAddress;
	private ObjectOutputStream out = null;
	private List<Email> emails = null;
	private Email email;
	
	public SendPendingMessages(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void run() {
		logger.info("Sending Pending Messages to: " + emailAddress);
		List<Email> emailsToRemove = new ArrayList<Email>();
	    try{
		      out = SuperCECServer.getEmailToObjectOutputStream().get(emailAddress);
		      emails = SuperCECServer.getUnSendableEmailsMap().get(emailAddress);
		      for(Email email: emails){
		    	  this.email = email;
		    	  out.writeObject(email);
		    	  out.flush();
		    	  emailsToRemove.add(email);
		    	  logger.info("Email with subject: "+email.getSubject()+"  has been sent to: " +emailAddress );
	      }
		} catch (SocketException e) {
			logger.severe("Sending of email with subject: "+ email.getSubject()+" failed for receiver: " + emailAddress);
			logger.severe(emailAddress + " Disconnected from the server!");
		} catch (EOFException e) {
			logger.severe(emailAddress + " Disconnected from the server!");
		} catch (Exception e) {
			logger.severe(emailAddress + " Disconnected from the server!");
			logger.severe(StackTrace.asString(e));
		}
	    finally{ 
	    	for (Email e: emailsToRemove) {
	    		emails.remove(e);
	    	}
	    	emailsToRemove = null;
	    	logger.info("Size of emails List after sending pending messages: " + emails.size());
	    }
	}

}
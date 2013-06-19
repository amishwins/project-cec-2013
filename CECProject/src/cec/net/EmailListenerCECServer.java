package cec.net;

import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.model.Email;
import cec.model.EmailBuilder;

/**
 * Server is running and listening to any incoming email.
 * For any email received, a list of recipients is built from to and cc fields,
 * and server try to send email to each recipients. for non existing user, a notice of failure
 * delivery is send back to the sender email address. For not connected user, the email 
 * is kept in a queue and will be delivered when recipient will connect.  
 *
 */
public class EmailListenerCECServer implements Runnable {
	
	static Logger logger = Logger.getLogger(EmailListenerCECServer.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( EmailListenerCECServer.class.getPackage().getName() ) );
    }
	private ObjectOutputStream out = null;

	public EmailListenerCECServer() {

	}
	
	public String removeSpaces(String s) {
		  StringTokenizer st = new StringTokenizer(s," ",false);
		  String t="";
		  while (st.hasMoreElements()) t += st.nextElement();
		  return t;
		}

	public void run() {
		logger.info("Thread: Listening for email Arrivals is started .....");
		Email newEmail;
			while (true) {
			try {
				
				newEmail = SuperCECServer.getArrivingEmailQueue().take();
				
				String toAddress = removeSpaces(newEmail.getTo());
				String ccAddress = removeSpaces(newEmail.getCC());
				Recipients recip = new Recipients(toAddress, ccAddress);

				Set<String> emailAddresses = recip.getListOfAllTargetRecipients();
				logger.info(emailAddresses.toString());
				
				for(String emailAddress: emailAddresses) {
					String addr = emailAddress.trim();
					out = SuperCECServer.getEmailToObjectOutputStream().get(addr);
					Email deliveryFailureNoticeEmail=null;
					if (null == out) {
						try {
						      logger.info(addr + " does not exist.");
						      out = SuperCECServer.getEmailToObjectOutputStream().get(newEmail.getFrom());
						      out.flush();
						      deliveryFailureNoticeEmail = buildNoticeEmail(newEmail, addr);
						      out.writeObject(deliveryFailureNoticeEmail);
						      logger.info("Delivery Failure Email to " + newEmail.getFrom()
								+ " has been sent.");
						} catch (SocketException e) {
							logger.severe("Delivery Failure Email sending failed for: " + newEmail.getFrom());
							(SuperCECServer.getUnSendableEmailsMap().get(newEmail.getFrom())).add(deliveryFailureNoticeEmail);
							logger.severe("Delivery Failure Email has been added to UnSendableQueue for: "+  newEmail.getFrom());							
     					}
					} else {
						try {
							out.writeObject(newEmail);
							out.flush();
							SuperCECServer.getSentEmailMap().put(newEmail.getId(), newEmail);
							logger.info("Email with subject: "+newEmail.getSubject() + " has been sent to " + addr);
						} catch (SocketException e) {
							logger.info("Sending of email with subject: "+ newEmail.getSubject()+" failed for receiver: " + addr);
							(SuperCECServer.getUnSendableEmailsMap().get(addr)).add(newEmail);
							printUnSendableEmailsMapEmails(addr);
							logger.info("Email with subject: "+newEmail.getSubject()+"  has been added to UnSendableQueue for: " + addr);
     					}
					}					
				}
				
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
			}
		}
	}

	private void printUnSendableEmailsMapEmails(String addr) {
		
		logger.info("Printing UnSendable Email List Size: "+SuperCECServer.getUnSendableEmailsMap().get(addr).size());
		for(Email email: (SuperCECServer.getUnSendableEmailsMap().get(addr))){
			logger.info(addr + " has email with subject : "+ email.getSubject());
		}
		
	}

	private Email buildNoticeEmail(Email email, String unknownRecipent) {
		EmailBuilder emailBuilder = new EmailBuilder();
		Email noticeEmail = emailBuilder.computeID().withTo(email.getFrom())
				.withFrom("CECSERVER@cec.com")
				.withBody(buildEmailBody(email, unknownRecipent))
				.withSubject(buildSubject()).computelastModifiedTime()
				.computeSentTime().build();
		return noticeEmail;
	}

	private String buildSubject() {
		String subject = "Delivery Failure Notice: Unknown Recipient";
		return subject;
	}

	private String buildEmailBody(Email email, String unknownRecipent) {
		StringBuilder emailBody = new StringBuilder();
		emailBody.append("\n The email which appears below could not be sent. Email address: "
				+ unknownRecipent + " does not exist on the server.");
		emailBody.append("\n\n To : " + unknownRecipent);
		emailBody.append("\n From: " + email.getFrom());
		emailBody.append("\n Subject: " + email.getSubject());
		emailBody.append("\n Sent Time: "
				+ email.getLastModifiedTimeNicelyFormatted());
		emailBody.append("\n Body: " + email.getBody());
		emailBody.append("\n");
		return emailBody.toString();
	}
}

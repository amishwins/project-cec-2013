package cec.net;

import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.Set;
import java.util.StringTokenizer;

import cec.model.Email;
import cec.model.EmailBuilder;

public class EmailListenerCECServer implements Runnable {
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
		Email newEmail;
		while (true) {
			try {
				System.out.println("Listening for email Arrivals .....");
				newEmail = SuperCECServer.getArrivingEmailQueue().take();
				
				String toAddress = removeSpaces(newEmail.getTo());
				String ccAddress = removeSpaces(newEmail.getCC());
				Recipients recip = new Recipients(toAddress, ccAddress);

				Set<String> emailAddresses = recip.getListOfAllTargetRecipients();
				System.out.println(emailAddresses);
				
				for(String emailAddress: emailAddresses) {
					String addr = emailAddress.trim();

					out = SuperCECServer.getEmailToObjectOutputStream().get(addr);
					if (null == out) {
						out = SuperCECServer.getEmailToObjectOutputStream().get(newEmail.getFrom());
						Email deliveryFailureNoticeEmail = buildNoticeEmail(newEmail, addr);
						out.writeObject(deliveryFailureNoticeEmail);
						System.out.println("Delivery Failure Email " + newEmail.getFrom()
								+ " has been sent.");
					} else {
						// only handling 1 email for now
						// TODO: make it handle more
						
						try {
							out.writeObject(newEmail);
							SuperCECServer.getSentEmailMap().put(newEmail.getId(), newEmail);
							System.out.println("Email " + addr + " has been sent.");
						} catch (SocketException e) {
							System.out.println("Email sending failed for: " + addr);
							// handle this!!!!
						}
					}					
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		emailBody.append("\n Below Email Could not be delivered as "
				+ unknownRecipent + " account does not exist!");
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

package cec.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import cec.model.Email;
import cec.model.EmailBuilder;

class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String emailAddress;

	public String toString() {
		return emailAddress;
	}
}

class ServerThreadPerClient implements Runnable {

	private String emailAddress;

	public ServerThreadPerClient(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void run() {
		System.out.println("Accepting Emails from this guy: " + emailAddress);
		while (true) {
			try {
				// Receiving email from some client... 
				Email e = (Email) SuperCECServer.getEmailToObjectInputStream()
						.get(emailAddress).readObject();
				System.out.println("is Email Added to Queue: "
						+ SuperCECServer.getArrivingEmailQueue().add(e));
				// Sending back Ack to client....
				System.out.println("Sending Ack to : "+emailAddress);
				Ack ack = new Ack(e.getId(),MessageType.EMAIL);
                SuperCECServer.getEmailToObjectOutputStream()
					.get(emailAddress).writeObject(ack); 				

                // System.out.println("Accepted Email for: " + e);
			} catch (EOFException e) {
				System.out.println(emailAddress
						+ " Disconnected from the server!");
				// e.printStackTrace();
				break;
			} catch (Exception e1) {
				System.out.println(emailAddress
						+ " Disconnected from the server!");
				e1.printStackTrace();
				break;
			}
		}
	}
}

class ClientAcceptor implements Runnable {
	ServerSocket server = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;

	public ClientAcceptor(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		System.out.println("Starting Connection Thread :.....");
		while (true) {

			try {
				System.out.println("Ready to accept connections...");
				Socket socket = server.accept();
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());

				HandShake handShake = (HandShake) in.readObject();
				SuperCECServer.getEmailToSocketMap().put(
						handShake.emailAddress, socket);
				SuperCECServer.getEmailToObjectInputStream().put(
						handShake.emailAddress, in);
				SuperCECServer.getEmailToObjectOutputStream().put(
						handShake.emailAddress, out);

				Set<String> emails = SuperCECServer.getEmailToSocketMap()
						.keySet();
				for (String email : emails) {
					System.out.println("Client connected. Email: " + email
							+ ". Socket: "
							+ SuperCECServer.getEmailToSocketMap().get(email));
				}

				// Spawn new task for the client which has just connected
				SuperCECServer.getExecutor().submit(
						new ServerThreadPerClient(handShake.emailAddress));

				System.out.println("Connection Accepted !!!");
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class ListenerForThingsInQueue implements Runnable {
	private ObjectOutputStream out = null;

	public ListenerForThingsInQueue() {

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

				HashSet<String> emailAddresses = recip.getListOfAllTargetRecipients();
				
				System.out.println(emailAddresses);
				
				for(String emailAddress: emailAddresses) {
					String addr = emailAddress.trim();

					out = SuperCECServer.getEmailToObjectOutputStream().get(addr);
					if (null == out) {
						out = SuperCECServer.getEmailToObjectOutputStream().get(newEmail.getFrom());
						Email deliveryFailureNoticeEmail = buildNoticeEmail(newEmail, newEmail.getTo());
						out.writeObject(deliveryFailureNoticeEmail);
						System.out.println("Delivery Failure Email " + newEmail.getFrom()
								+ " has been sent.");
					} else {
						// only handling 1 email for now
						// TODO: make it handle more
						
						try {
							out.writeObject(newEmail);
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

public class SuperCECServer {

	static LinkedBlockingDeque<Email> arrivingEmails = new LinkedBlockingDeque<>();
	static ConcurrentHashMap<String, Socket> emailToSocketMap = new ConcurrentHashMap<>();
	static ExecutorService executor = Executors.newCachedThreadPool();
	static ConcurrentHashMap<String, ObjectInputStream> emailToObjectInputStream = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, ObjectOutputStream> emailToObjectOutputStream = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String, ObjectInputStream> getEmailToObjectInputStream() {
		return emailToObjectInputStream;
	}

	public static ConcurrentHashMap<String, ObjectOutputStream> getEmailToObjectOutputStream() {
		return emailToObjectOutputStream;
	}

	public static ConcurrentHashMap<String, Socket> getEmailToSocketMap() {
		return emailToSocketMap;
	}

	public static ExecutorService getExecutor() {
		return executor;
	}

	public static LinkedBlockingDeque<Email> getArrivingEmailQueue() {
		return arrivingEmails;
	}

	public static void main(String[] args) throws IOException {

		ServerSocket server = null;
		try {
			server = new ServerSocket(7777);
		} catch (BindException e) {
			System.out.println("Server is already running on this port!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		executor.submit(new ClientAcceptor(server));

		executor.submit(new ListenerForThingsInQueue());

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

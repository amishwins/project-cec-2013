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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.model.Email;
import cec.model.EmailImpl;
import cec.model.FolderFactory;
import cec.model.Meeting;
import cec.model.MeetingBuilder;
import cec.model.MeetingChangeSet;

class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String emailAddress;

	public String toString() {
		return emailAddress;
	}
}


class SentEmailWriteToSysout implements Runnable {
	static Logger logger = Logger.getLogger(SentEmailWriteToSysout.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SentEmailWriteToSysout.class.getPackage().getName() ) );
    }
	public void run() {
		while(true) {
			logger.info(String.valueOf(SuperCECServer.getSentEmailMap().size()));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.severe(StackTrace.asString(e));
			}
		}
	}
}

class SentEmailCleanupThread implements Runnable {
	static Logger logger = Logger.getLogger(SentEmailCleanupThread.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SentEmailCleanupThread.class.getPackage().getName() ) );
    }
	public void run() {
		while(true) {
			try {
				Ack ack = SuperCECServer.getArrivingEmailAcks().take();
				
				SuperCECServer.getSentEmailMap().remove(ack.getId());
				
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
			}
		}
		
	}
}

class ServerThreadPerClient implements Runnable {

	static Logger logger = Logger.getLogger(ServerThreadPerClient.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( ServerThreadPerClient.class.getPackage().getName() ) );
    }
	
	private String emailAddress;

	public ServerThreadPerClient(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void run() {
		logger.info("Accepting Emails from this cleint: " + emailAddress);
		while (true) {
			try {
				// Receiving objects
					// handle meeting request
				Object obj = SuperCECServer.getEmailToObjectInputStream()
						.get(emailAddress).readObject();
				
				if (obj instanceof Email) {
					// handle emails

					EmailImpl e = (EmailImpl) obj;
					if(!e.isMeetingEmail()){
						logger.info("is Email " + e.getSubject()+ " Added to Queue: " + SuperCECServer.getArrivingEmailQueue().add(e));
						logger.info("CECServer Sending Ack to : "+ emailAddress+" for email subject: "+e.getSubject());
						Ack ack = new Ack(e.getId(),MessageType.EMAIL);
		                SuperCECServer.getEmailToObjectOutputStream().get(emailAddress).writeObject(ack);
					}else{
						logger.info("Going to build a Meeting object from the email meeting invitation");
						buildMeeting(e);
					}
					 	

				} else if ((obj instanceof Ack)) {
					// handle acks
					Ack ack = (Ack) obj;
					handleAck(ack);
					// delete the thing that we were saving for this ack (regular email, email for meeting, 
					// or changeset)
					
				} else if ((obj instanceof MeetingChangeSet)) {
					// handle ChangeSets

				}
			} catch (SocketException e) {
				logger.info(emailAddress
						+ " is Disconnected from the server!");
				break;
			}  catch (EOFException e) {
				logger.info(emailAddress
						+ " Disconnected from the server!");
				break;
			} catch (Exception e) {
				logger.info(emailAddress
						+ " Disconnected from the server!");
				logger.severe(StackTrace.asString(e));
				break;
			}
		}
	}
	private void buildMeeting(EmailImpl e) {
		MeetingBuilder mb = new MeetingBuilder();
		logger.fine(e.getSubject());
		logger.fine(e.getBody());
				
		String[] bodyLines = e.getBody().split("\n");
		for(String bodyLine: bodyLines){
			logger.fine(bodyLine);
		}
		String subject = bodyLines[2].split(":",2)[1].trim();
		String location = bodyLines[3].split(":",2)[1].trim();
		String startDateTime = bodyLines[4].split(":",2)[1].trim();
		String endDateTime = bodyLines[5].split(":",2)[1].trim();
		String body = bodyLines[7];
          
		logger.fine(subject);
		logger.fine(location);
		logger.fine(startDateTime);
		logger.fine(endDateTime);
		
		String startDate = startDateTime.split("at:",2)[0].trim();
		String startTime = startDateTime.split("at:",2)[1].trim();
		String endDate = endDateTime.split("at:",2)[0].trim();
		String endTime = endDateTime.split("at:",2)[1].trim();
		
		logger.fine(startDate);
		logger.fine(startTime);
		logger.fine(endDate);
		logger.fine(endTime);
		logger.fine(body);
       
		Meeting meeting = mb.withId(e.getId())
							.withFrom(e.getFrom())
							.withAttendees(e.getTo())
							.withSubject(subject)
							.withPlace(location)
							.withStartDate(formatDateForMeeting(startDate))
							.withEndDate(formatDateForMeeting(endDate))
							.withStartTime(startTime)
							.withEndTime(endTime)
							.withBody(body)
							.withLastModifiedTime(e.getLastModifiedTime())
							.withSentTime(e.getSentTime())
							.withParentFolder(FolderFactory.getFolder("Unknown")).build();
		logger.info("Here is the Meeting Object for email that above received:");
		logger.info(meeting.toString());
      }

	private String formatDateForMeeting(String date) {
		Date dateToBeFormatted = new Date(date);
		logger.fine(dateToBeFormatted.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate =  sdf.format(dateToBeFormatted);
    	logger.fine(formattedDate);
		return formattedDate;
	}

	private void handleAck(Ack ack) {
		if (ack.getMsgType() == MessageType.EMAIL || ack.getMsgType() == MessageType.MEETING ) {
			try {
				logger.info("Putting ack on EmailAckQueue");
				SuperCECServer.getArrivingEmailAcks().put(ack);
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
			}
		
		} else if (ack.getMsgType() == MessageType.CHANGESET) {
			// what does the server do with changesets? Does it apply immediately? 
		}
		
	}
}

class ClientAcceptor implements Runnable {
	static Logger logger = Logger.getLogger(ClientAcceptor.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( ClientAcceptor.class.getPackage().getName() ) );
    }
	
	ServerSocket server = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;

	public ClientAcceptor(ServerSocket server) {
		this.server = server;
	}

	public void run() {
		logger.info("Thread to Accept Connections is started .....");
		while (true) {

			try {
				logger.info("Ready to accept new connections...");
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
				logger.info(handShake.emailAddress+" is connected!!!");
				Set<String> emails = SuperCECServer.getEmailToSocketMap()
						.keySet();
				for (String email : emails) {
					logger.info("Client connected. Email: " + email
							+ ". Socket: "
							+ SuperCECServer.getEmailToSocketMap().get(email));
				}

				// Spawn new task for the client which has just connected
				SuperCECServer.getExecutor().submit(
						new ServerThreadPerClient(handShake.emailAddress));

			}  catch (Exception e){
				logger.severe(StackTrace.asString(e));
				break;
			}
		}
	}
}

public class SuperCECServer {
	
	static Logger logger = Logger.getLogger(SuperCECServer.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( SuperCECServer.class.getPackage().getName() ) );
    }

	static LinkedBlockingDeque<Email> arrivingEmails = new LinkedBlockingDeque<>();
	static LinkedBlockingDeque<Ack> arrivingEmailAcks = new LinkedBlockingDeque<>();
	static ConcurrentHashMap<UUID, Email> sentEmails = new ConcurrentHashMap<>();
	
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
	
	public static LinkedBlockingDeque<Ack> getArrivingEmailAcks() {
		return arrivingEmailAcks;
	}
	
	public static ConcurrentHashMap<UUID, Email> getSentEmailMap() {
		return sentEmails;
	}

	public static void main(String[] args) throws IOException {

		ServerSocket server = null;
		try {
			server = new ServerSocket(7777);
		} catch (BindException e) {
			logger.info("CECServer is already running on this port!");
			logger.severe(StackTrace.asString(e));
		} catch (Exception e){
			logger.severe(StackTrace.asString(e));
		}

		executor.submit(new ClientAcceptor(server));
		executor.submit(new EmailListenerCECServer());
		executor.submit(new SentEmailCleanupThread());
		//executor.submit(new SentEmailWriteToSysout());

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);

		} catch (Exception e) {
			logger.severe(StackTrace.asString(e));
		}
	}
}

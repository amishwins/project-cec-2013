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
		logger.info("Accepting Emails from this guy: " + emailAddress);
		while (true) {
			try {
				// Receiving objects
					// handle meeting request
				Object obj = SuperCECServer.getEmailToObjectInputStream()
						.get(emailAddress).readObject();
				
				if (obj instanceof Email) {
					// handle emails

					Email e = (Email) obj;
					logger.info("is Email Added to Queue: " + SuperCECServer.getArrivingEmailQueue().add(e));
					logger.info("Sending Ack to : "+ emailAddress);
					Ack ack = new Ack(e.getId(),MessageType.EMAIL);
	                SuperCECServer.getEmailToObjectOutputStream().get(emailAddress).writeObject(ack); 	

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
						+ " Disconnected from the server!");
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
		logger.info("Starting Connection Thread :.....");
		while (true) {

			try {
				logger.info("Ready to accept connections...");
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
					logger.info("Client connected. Email: " + email
							+ ". Socket: "
							+ SuperCECServer.getEmailToSocketMap().get(email));
				}

				// Spawn new task for the client which has just connected
				SuperCECServer.getExecutor().submit(
						new ServerThreadPerClient(handShake.emailAddress));

				logger.info("Connection Accepted !!!");
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
			logger.info("Server is already running on this port!");
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

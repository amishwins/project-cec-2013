package cec.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import cec.config.CECConfigurator;
import cec.exceptions.StackTrace;
import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.EmailImpl;
import cec.model.Folder;
import cec.model.FolderFactory;

public class NetworkHelper {
	
	
	
	static Logger logger = Logger.getLogger(NetworkHelper.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( NetworkHelper.class.getPackage().getName() ) );
    }
    
    ConcurrentHashMap<UUID, MeetingLock> waitingForServerResponseLocks = new ConcurrentHashMap<>(); 
    volatile CommunicationChangeSet receivedCCS;
    
    static NetworkHelper instance;

    static public NetworkHelper getReference() {
    	if (instance == null) {
    		instance = new NetworkHelper();
    	}
    	
    	return instance;
    }

	public static boolean isConnectedToServer() {	
			return (clientSocket != null);	
	}

    private NetworkHelper() {
    	
    }
	
	class ListenerForMessagesFromServer implements Runnable {
		public void run() {

			while (!stop) {
				try {
					Object obj = ois.readObject();
					if (obj instanceof Email) {
						Email email = (Email) obj;
						handleEmail(email);
					}
					
					else if ((obj instanceof Ack)) {
						Ack ack = (Ack) obj;
						handleAck(ack);
					}
					
					else if ((obj instanceof CommunicationChangeSet)) { 
						receivedCCS = (CommunicationChangeSet) obj;
						// oh god
						waitingForServerResponseLocks.get(receivedCCS.getId()).notify();
					}
					
				} catch (EOFException e) {
					logger.info("Disconnected from the server!");
					break;
				} catch (SocketException e) {
					logger.info("Disconnected from the server!");
					break;					
				} catch (Exception e) {
					logger.info("Disconnected from the server!");
					logger.severe(StackTrace.asString(e));
					break;
				}

			}
		}

		private void handleEmail(Email email) {
			EmailBuilder mailBuilder = new EmailBuilder();
			boolean isMeetingEmail = ((EmailImpl) email).isMeetingEmail();

			Email newEmail = mailBuilder.withId(email.getId()).withFrom(email.getFrom())
					.withTo(email.getTo()).withSubject(email.getSubject())
					.withBody(email.getBody()).withCC(email.getCC())
					.computelastModifiedTime().computeSentTime()
					.withInboxParentFolder()
					.withIsMeetingEmail(isMeetingEmail)
					.build();

			newEmail.saveToInboxFolder();
			
			// send an ack back
			Ack recieved = new Ack(newEmail.getId(), MessageType.EMAIL);
			try {
				logger.info("Sending Ack back to server");
				oos.writeObject(recieved);
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));				
			}
		}
	}

	private void handShake() throws IOException {
		HandShake hs = new HandShake();
		hs.emailAddress = config.getClientEmailAddress();
		oos.writeObject(hs);
	}

	public void handleAck(Ack ack) {
		Ack recievedAck = ack;
		if (recievedAck.getMsgType() == MessageType.EMAIL) {
			EmailBuilder mailBuilder = new EmailBuilder();
			Email existingEmail = mailBuilder
					.withId(ack.getId())
					.withParentFolder(
							FolderFactory.getFolder(CECConfigurator
									.getReference().get("Outbox"))).build();
			existingEmail.move(FolderFactory.getFolder(CECConfigurator
					.getReference().get("Sent")));
		}
	}

	// TODO: Do we need to make the ExecutorServer static?
	static Socket clientSocket;
	static ObjectInputStream ois;
	static ObjectOutputStream oos;
	CECConfigurator config = CECConfigurator.getReference();
	volatile boolean stop = false;
	static ExecutorService exec;

	public static Socket getSocket() {
		return clientSocket;
	}

	public void connectToServer() {

		if (!isConnectedToServer()){		
			try {
				clientSocket = new Socket(config.get("ServerName"),
						Integer.parseInt(config.get("ServerPort")));
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
				ois = new ObjectInputStream(clientSocket.getInputStream());
				handShake();			
				logger.info("Connected to the server!");			
				exec = Executors.newCachedThreadPool();
				exec.submit(new ListenerForMessagesFromServer());
	            sendPendingEmails();
				/*
				 * try { exec.awaitTermination(1, TimeUnit.DAYS);
				 * 
				 * } catch (Exception e) { e.printStackTrace(); }
				 */
	
			} catch (ConnectException e) {
				logger.info("Unable to connect to server. Seems like server is down!");
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));				
			}
		}
	}

	private void sendPendingEmails() {
		Folder outboxFolder = FolderFactory.getFolder(CECConfigurator
				.getReference().get("Outbox"));
		Iterable<Email> emails = outboxFolder.loadEmails();
		for(Email email : emails){
			email.send();
		}
	}

	public void sendEmail(Email email) {
		final Email myEmail = email;
		exec.submit(new Runnable() {
			public void run() {
				try {
					oos.writeObject(myEmail);
				} catch (Exception e) {
					logger.severe(StackTrace.asString(e));				
				}
			}
		});
	}
	
	public void sendChangeSet(CommunicationChangeSet ccs) {
		if (NetworkHelper.isConnectedToServer()) {
			try {
				if (ccs.isChange()){
					// setup the lock to return control back to the user only when the server 
					// sends either CHANGE_ACCEPTED, or CHANGE_REJECTED
					MeetingLock ml = new MeetingLock();
					waitingForServerResponseLocks.put(ccs.getId(), ml);
					oos.writeObject(ccs);
					
					logger.info("We have locked the main thread. Oh god!");
					
					
					logger.info("We have unlocked the main thread.");
					
					
				}
				
				else {
					oos.writeObject(ccs);
				}
					
				
			} catch (IOException e) {
				logger.severe(StackTrace.asString(e));	
			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));				
			}
		}
		else {
			logger.info("You are currently not connected to the CEC network.");
		}
	}

	public void disconnectFromServer() {
		stopClient();
		if (null != exec)
			exec.shutdown();
		// TODO do we have to awaitTermination?
		Cleanup.closeQuietly(ois);
		Cleanup.closeQuietly(oos);
		Cleanup.closeQuietly(clientSocket);
		clientSocket = null;
	}

	private void stopClient() {
		stop = true;
	}

}
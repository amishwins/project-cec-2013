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

/**
 * @author r_honvo
 *
 */
public class NetworkHelper {
	
	
	
	static Logger logger = Logger.getLogger(NetworkHelper.class.getName()); 

    static { 
        logger.setParent( Logger.getLogger( NetworkHelper.class.getPackage().getName() ) );
    }
    
    static ConcurrentHashMap<UUID, CommunicationChangeSet> changeSetsForMeetings = new ConcurrentHashMap<>();
 // The Thread Pool we will use is the Executor Service, a really awesome implementation
 	static ExecutorService clientExecutor = Executors.newCachedThreadPool();
        
    static NetworkHelper instance;

    static public NetworkHelper getReference() {
    	if (instance == null) {
    		instance = new NetworkHelper();
    	}
    	
    	return instance;
    }

	/**
	 * @return
	 */
	public static boolean isConnectedToServer() {	
			return (clientSocket != null);	
	}

    private NetworkHelper() {
    	
    }
	
	/**
	 * While server is running, client receive email or acknowledgment or a 
	 * Communication Change Set for a meeting he is trying to update	
	 * for an email, it has to be saved in the inbox folder and an Ack has to be sent back to server
	 * for an acknowledgment, server confirm reception of email client just sent and then email is moved
	 * from outbox folder to sent folder on the client side.
	 */
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
						CommunicationChangeSet receivedCCS;
						receivedCCS = (CommunicationChangeSet) obj;
						logger.info("Putting the received change set from the server onto the map. It is of type: " + receivedCCS.getState());
						changeSetsForMeetings.put(receivedCCS.getId(), receivedCCS);						
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
			
			logger.info("Spawning a new Task to inform client to upadate itself!");
			clientExecutor.submit(new UpdateEmailClientView());
			
			
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

	/**
	 * This function is responsible of handling acknowledgment received as parameter
	 * If the received acknowledgment from server is an email acknowledgment, the existing email is 
	 * moved from the outbox folder to the sent folder
	 * @param ack
	 */
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

	static Socket clientSocket;
	volatile static ObjectInputStream ois;
	volatile static ObjectOutputStream oos;
	CECConfigurator config = CECConfigurator.getReference();
	volatile boolean stop = false;
	static ExecutorService exec;

	
	
	/**
	 * Return the user socket
	 * @return
	 */
	public static Socket getSocket() {
		return clientSocket;
	}

	/**
	 * Client try to connection to the server
	 * with a server name and a port number
	 */
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

	/**
	 * Client try to perform the operation of sending 
	 * Email object to the server
	 * @param email
	 */
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
	
	/**
	 * Tell if the change user is trying to make is accepted or no by server.
	 * Return the change set from the server
	 * 
	 * @param ccs
	 * @return
	 */
	public CommunicationChangeSet sendChangeSet(CommunicationChangeSet ccs) {
		if (NetworkHelper.isConnectedToServer()) {
			try {
				if (ccs.isChange()){
					// setup the lock to return control back to the user only when the server 
					// sends either CHANGE_ACCEPTED, or CHANGE_REJECTED
					oos.writeObject(ccs);
					
					logger.info("We have locked the main thread. Oh god!");
					
					long start_time = System.currentTimeMillis(); 
					long elapsed_time = 0L;
					CommunicationChangeSet received;
					while(true) {
						received = changeSetsForMeetings.get(ccs.getId());
						changeSetsForMeetings.remove(ccs.getId());
						if (received == null) {
							Thread.sleep(100);
							logger.info("Waiting...");							
						}
						else {
							logger.info("Was able to get the change set response from the server for this meeting");
							break;
						}
						elapsed_time = System.currentTimeMillis() - start_time;
						if(elapsed_time > 3000L) {
							throw new RuntimeException("Server did not respond with a Change Set!");
						}
						
					}
					
					logger.info("We have unlocked the main thread.");
					// Make sure that the client updates his things
					return received;
					
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
		return null;
	}


	public void disconnectFromServer() {
		stopClient();
		if (null != exec)
			exec.shutdown();
		Cleanup.closeQuietly(ois);
		Cleanup.closeQuietly(oos);
		Cleanup.closeQuietly(clientSocket);
		clientSocket = null;
	}

	private void stopClient() {
		stop = true;
	}

}
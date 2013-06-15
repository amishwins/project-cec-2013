package cec.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cec.config.CECConfigurator;
import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.Folder;
import cec.model.FolderFactory;

public class NetworkHelper {

	public static boolean isConnectedToServer() {
		return (clientSocket != null);
	}

	class ListenerForMessagesFromServer implements Runnable {
		public void run() {

			while (!stop) {
				try {
					Object obj = ois.readObject();
					if (obj instanceof Email) {
						Email email = (Email) obj;
						handleEmail(email);
					} else if ((obj instanceof Ack)) {
						Ack ack = (Ack) obj;
						handleAck(ack);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (EOFException e) {
					System.out.println("Disconnected from the server!");
					break;
				} catch (Exception e) {
					System.out.println("Disconnected from the server!");
					break;
				}

			}
		}

		private void handleEmail(Email email) {
			EmailBuilder mailBuilder = new EmailBuilder();

			Email newEmail = mailBuilder.computeID().withFrom(email.getFrom())
					.withTo(email.getTo()).withSubject(email.getSubject())
					.withBody(email.getBody()).withCC(email.getCC())
					.computelastModifiedTime().computeSentTime()
					.withInboxParentFolder().build();

			newEmail.saveToInboxFolder();
			
			// send an ack back
			Ack recieved = new Ack(newEmail.getId(), MessageType.EMAIL);
			try {
				System.out.println("Sending Ack back to server");
				oos.writeObject(recieved);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void handShake() throws IOException {
		HandShake hs = new HandShake();
		hs.emailAddress = config.getClientEmailAddress();
		// ObjectOutputStream outputStream = new
		// ObjectOutputStream(clientSocket.getOutputStream() );
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

		try {
			clientSocket = new Socket(config.get("ServerName"),
					Integer.parseInt(config.get("ServerPort")));
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
			ois = new ObjectInputStream(clientSocket.getInputStream());
			handShake();
			exec = Executors.newCachedThreadPool();
			exec.submit(new ListenerForMessagesFromServer());
            sendPendingEmails();
			/*
			 * try { exec.awaitTermination(1, TimeUnit.DAYS);
			 * 
			 * } catch (Exception e) { e.printStackTrace(); }
			 */

		} catch (ConnectException e) {
			System.out
					.println("Unable to connect to server. Seems like server is down!");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void disconnectFromServer() {
		// clientSocket = null;
		// ois = null;
		// oos = null;
		stopClient();
		exec.shutdown();
		// TODO do we have to awaitTermination?
		Cleanup.closeQuietly(ois);
		Cleanup.closeQuietly(oos);
		Cleanup.closeQuietly(clientSocket);
	}

	private void stopClient() {
		stop = true;
	}

}
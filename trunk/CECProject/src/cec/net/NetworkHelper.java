package cec.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cec.config.CECConfigurator;
import cec.model.Email;
import cec.model.EmailBuilder;



public class NetworkHelper {
	
	class ListenerForMessagesFromServer implements Runnable {
		public void run() {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(NetworkHelper.getSocket().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			while(!stop) {
				try {
					// TODO: this should be moved to a new method to handle all the different types
					Object obj = ois.readObject();
					if (obj instanceof Email) {
						Email email = (Email) obj;
						handleEmail(email);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

		private void handleEmail(Email email) {
			EmailBuilder mailBuilder = new EmailBuilder();
			
			Email newEmail  =  mailBuilder.computeID().withFrom(email.getFrom())
				.withTo(email.getTo())
				.withSubject(email.getSubject())
				.withBody(email.getBody()).withCC(email.getCC())
				.computelastModifiedTime().computeSentTime()				
				.withInboxParentFolder().build();	
			
			newEmail.saveToInboxFolder();
		}
	}
	
	static Socket clientSocket;
	CECConfigurator config = CECConfigurator.getReference();
	volatile boolean stop = false;
		
	public static Socket getSocket() {
		return clientSocket;
	}
	
	public void connectToServer() {
		
	    try {
			clientSocket = new Socket(config.get("ServerName"), 
					Integer.parseInt(config.get("ServerPort")));
			
			handShake();
			
			ExecutorService exec = Executors.newFixedThreadPool(2);
			exec.submit(new ListenerForMessagesFromServer());
			
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void disconnectFromServer() {
		stopClient();
		Cleanup.closeQuietly(clientSocket);
	}
	
	private void handShake() throws IOException {
		HandShake hs = new HandShake();
        hs.emailAddress = config.getClientEmailAddress();
        ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream() );
        outputStream.writeObject(hs);	    
	}
	
	private void stopClient() {
		stop = true;
	}

}
package cec.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;
import cec.model.Email;

/**
 * The Class ServerClientAcceptor handles accepting  connections from client.
 * client handshakes with the servers sending its email Address 
 * and server register client on the server.
 */
public class ServerClientAcceptor implements Runnable {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(ServerClientAcceptor.class.getName());

	static {
		logger.setParent(Logger.getLogger(ServerClientAcceptor.class.getPackage().getName()));
	}

	/** The server. */
	ServerSocket server = null;
	
	/** The in. */
	ObjectInputStream in = null;
	
	/** The out. */
	ObjectOutputStream out = null;

	/**
	 * Instantiates a new server client acceptor.
	 *
	 * @param server the server
	 */
	public ServerClientAcceptor(ServerSocket server) {
		this.server = server;
	}
	
	/**
	 * This thread accepts new client connection. registers it to the server.
	 * This thread is also responsible for setting up the server data.
	 * It also holds the responsibility to send pending email(s) to client.
	 * @param server the server
	 */
	public void run() {
		logger.info("Thread to Accept Connections is started .....");
		while (true) {

			try {
				logger.info("Ready to accept new connections...");
				Socket socket = server.accept();
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());

				HandShake handShake = (HandShake) in.readObject();
				setupServerMaps(socket, handShake);
				
				logCurrentlyRegisteredUsers();
				
				for(Email email:SuperCECServer.getUnSendableEmailsMap().get(handShake.emailAddress)){
					logger.info(handShake.emailAddress+ " has email with subject: " + email.getSubject());					
				}
				
				
				
				// Spawn a new task if there are pending messages for this newly joined person.
                if((SuperCECServer.getUnSendableEmailsMap().get(handShake.emailAddress)).size()>0){
				   SuperCECServer.getExecutor().submit(new SendPendingMessages(handShake.emailAddress));
                }
				// Spawn new task for the client which has just connected
				SuperCECServer.getExecutor().submit(new ServerThreadPerClient(handShake.emailAddress));
				

			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
				break;
			}
		}
	}

	/**
	 * Log currently registered users.
	 */
	private void logCurrentlyRegisteredUsers() {
		Set<String> emails = SuperCECServer.getEmailToSocketMap().keySet();
		for (String email : emails) {
			logger.info("Client connected. Email: " + email
					+ ". Socket: "
					+ SuperCECServer.getEmailToSocketMap().get(email)
					+ " in stream: " + SuperCECServer.getEmailToObjectInputStream().get(email) + ". Out stream: " +  SuperCECServer.getEmailToObjectOutputStream().get(email)
					); 
					
		}
	}

	/**
	 * Setup server maps.
	 *
	 * @param socket the socket
	 * @param handShake the hand shake
	 */
	private void setupServerMaps(Socket socket, HandShake handShake) {
		SuperCECServer.getEmailToSocketMap().put(handShake.emailAddress, socket);
		SuperCECServer.getEmailToObjectInputStream().put(handShake.emailAddress, in);
		SuperCECServer.getEmailToObjectOutputStream().put(handShake.emailAddress, out);
		if((SuperCECServer.getUnSendableEmailsMap().get(handShake.emailAddress))==null){
    		SuperCECServer.getUnSendableEmailsMap().put(handShake.emailAddress, new ArrayList<Email>());
		}
		logger.info(handShake.emailAddress + " is connected!!!");
	}
}
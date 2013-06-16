package cec.net;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Logger;

import cec.exceptions.StackTrace;

public class ServerClientAcceptor implements Runnable {
	static Logger logger = Logger.getLogger(ServerClientAcceptor.class.getName());

	static {
		logger.setParent(Logger.getLogger(ServerClientAcceptor.class.getPackage().getName()));
	}

	ServerSocket server = null;
	ObjectInputStream in = null;
	ObjectOutputStream out = null;

	public ServerClientAcceptor(ServerSocket server) {
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
				setupServerMaps(socket, handShake);
				logCurrentlyRegisteredUsers();

				// Spawn new task for the client which has just connected
				SuperCECServer.getExecutor().submit(new ServerThreadPerClient(handShake.emailAddress));

			} catch (Exception e) {
				logger.severe(StackTrace.asString(e));
				break;
			}
		}
	}

	private void logCurrentlyRegisteredUsers() {
		Set<String> emails = SuperCECServer.getEmailToSocketMap().keySet();
		for (String email : emails) {
			logger.info("Client connected. Email: " + email
					+ ". Socket: "
					+ SuperCECServer.getEmailToSocketMap().get(email));
		}
	}

	private void setupServerMaps(Socket socket, HandShake handShake) {
		SuperCECServer.getEmailToSocketMap().put(handShake.emailAddress, socket);
		SuperCECServer.getEmailToObjectInputStream().put(handShake.emailAddress, in);
		SuperCECServer.getEmailToObjectOutputStream().put(handShake.emailAddress, out);
		logger.info(handShake.emailAddress + " is connected!!!");
	}
}
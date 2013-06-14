package cec.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import cec.model.Email;

class EmailTest implements Serializable {
	private static final long serialVersionUID = 1L;
	public UUID id;
	public String to;
	
	public String toString(){
		return to.toString();
	}
}

class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String emailAddress;
	
	public String toString(){
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
		while(true) {
			try {
				Email e = (Email) SuperCECServer.getEmailToObjectInputStream().get(emailAddress).readObject();
				System.out.println("is Email Added to Queue: "+ SuperCECServer.getArrivingEmailQueue().add(e));
				System.out.println("Accepted Email for: " + e);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
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
		while(true) {
			
			try {
				System.out.println("Ready to accept connections...");
				Socket socket = server.accept();
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				
				HandShake handShake = (HandShake) in.readObject();
				SuperCECServer.getEmailToSocketMap().put(handShake.emailAddress, socket);
				SuperCECServer.getEmailToObjectInputStream().put(handShake.emailAddress, in);
				SuperCECServer.getEmailToObjectOutputStream().put(handShake.emailAddress, out);
				
				Set<String> emails = SuperCECServer.getEmailToSocketMap().keySet();
				for(String email: emails){
					System.out.println("Client connected. Email: "+email+". Socket: "+ SuperCECServer.getEmailToSocketMap().get(email));
				}
				
				// Spawn new task for the client which has just connected
				SuperCECServer.getExecutor().submit(new ServerThreadPerClient(handShake.emailAddress));

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
	
	public void run() {
		Email newEmail;
		while(true) {
			try {
				newEmail = SuperCECServer.getArrivingEmailQueue().take();
				out = SuperCECServer.getEmailToObjectOutputStream().get(newEmail.getTo());

				System.out.println("Listening for email Arrivals ....." );
				//System.out.println("Size of Deque: "+arrivals.size() );

				// only handling 1 email for now
				// TODO: make it handle more

				out.writeObject(newEmail);
				System.out.println("Email " + newEmail.getTo() + " has been sent." );
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


public class SuperCECServer {
	
	static LinkedBlockingDeque<Email> arrivingEmails = new LinkedBlockingDeque<>();
	static ConcurrentHashMap<String, Socket> emailToSocketMap = new ConcurrentHashMap<>();
	static ExecutorService executor = Executors.newCachedThreadPool();
	static ConcurrentHashMap<String, ObjectInputStream> emailToObjectInputStream  = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, ObjectOutputStream> emailToObjectOutputStream  = new ConcurrentHashMap<>();
	
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

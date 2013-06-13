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


class Email implements Serializable {
	private static final long serialVersionUID = 1L;
	public UUID id;
	public String to;
	
	public String toString(){
		return to.toString();
	}
}


class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String email;
	
	
	public String toString(){
		return email.toString();
	}
}


class ServerThreadPerClient implements Runnable {
	ObjectInputStream in = null; 

	private Socket s = null;
	public ServerThreadPerClient(Socket s, ObjectInputStream in) {
		this.s = s;
		this.in = in;
	}
	public void run() {
		System.out.println("Accepting Emails from socket : "+ s);
		while(true) {
			try {
				Email e = (Email) in.readObject();
				System.out.println("is Email Added to Queue: "+SuperCECServer.getArrivingEmailQueue().add(e));
				System.out.println("Accepted Email for  "+ e +" From Socket : "+s);
				//System.out.println("Size of Deque: "+SuperCECServer.getArrivingEmailQueue().size());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} 
			
		}
	}
}

class ClientsConnecting implements Runnable {
	ServerSocket server = null;
	ObjectInputStream in = null; 
	ConcurrentHashMap<String, Socket> map;
	
	public ClientsConnecting(ServerSocket server, ConcurrentHashMap<String, Socket> map) {
		this.server = server;
		this.map = map;
	}
	public void run() {
		System.out.println("Starting Connection Thread :.....");
		while(true) {
			
			try {
				System.out.println("Accepting Connection !!!");
				Socket s = server.accept();
				in = new ObjectInputStream(s.getInputStream());
				HandShake h = (HandShake) in.readObject();
				map.put(h.email, s);
				
				System.out.println("Before: ");
				Set<String> emails = map.keySet();
				for(String email: emails){
					System.out.println("Email: "+email+" has been assigned Socket :"+ map.get(email));
				}
				System.out.println("After: ");
				
				SuperCECServer.getExecutor().submit(new ServerThreadPerClient(s,in));
         		System.out.println("Connection Accepted !!!");
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

class ListenerForThingsInQueue implements Runnable {
	private LinkedBlockingDeque<Email> arrivals;
	private ObjectOutputStream out = null;
	
	public ListenerForThingsInQueue(LinkedBlockingDeque<Email> arrivals) {
		this.arrivals = arrivals;
	}
	public void run() {
		while(true) {
			try {
				System.out.println("Listening for email Arrivals ....." );
				//System.out.println("Size of Deque: "+arrivals.size() );
				Email newEmail = arrivals.take();
				// only handling 1 email for now
				// TODO: make it handle more
				Socket s = SuperCECServer.getClientMap().get(newEmail.to);
				out = new ObjectOutputStream(s.getOutputStream()); 
				out.writeObject(newEmail);
				System.out.println("Email "+ newEmail + " has been sent to Socket " +s );
				
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
	static ConcurrentHashMap<String, Socket> clientMap = new ConcurrentHashMap<>();
	static ExecutorService executor = Executors.newCachedThreadPool();

	public static ConcurrentHashMap<String, Socket> getClientMap() {
		return clientMap;
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
			server = new ServerSocket(4444);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		executor.submit(new ClientsConnecting(server, clientMap));
		
		executor.submit(new ListenerForThingsInQueue(arrivingEmails));

//		executor.shutdown();

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

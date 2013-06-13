package cec.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
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
}


class HandShake implements Serializable {
	private static final long serialVersionUID = 1L;
	public String email;
}


class ServerThreadPerClient implements Runnable {
	ObjectInputStream in = null; 
	

	private Socket s = null;
	public ServerThreadPerClient(Socket s) {
		this.s = s;
	}
	public void run() {
		while(true) {
			try {
				in = new ObjectInputStream(s.getInputStream());
				Email e = (Email) in.readObject();
				SuperCECServer.getArrivingEmailQueue().add(e);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
		while(true) {
			
			try {
				Socket s = server.accept();
				in = new ObjectInputStream(s.getInputStream());
				HandShake h = (HandShake) in.readObject();
				map.put(h.email, s);
				SuperCECServer.getExecutor().submit(new ServerThreadPerClient(s));
				
			
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
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
				Email newEmail = arrivals.take();
				// only handling 1 email for now
				// TODO: make it handle more
				Socket s = SuperCECServer.getClientMap().get(newEmail.to);
				out = new ObjectOutputStream(s.getOutputStream()); 
				out.writeObject(newEmail);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
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

		executor.shutdown();
		
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}

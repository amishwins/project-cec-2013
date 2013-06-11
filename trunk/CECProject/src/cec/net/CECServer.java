package cec.net;
import java.net.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.io.*;

public class CECServer {
	private static CECServer instance;
	ConcurrentMap<Socket, String> clientSocketToEmailAddress;
	
	private CECServer() {
		clientSocketToEmailAddress = new ConcurrentHashMap<>();
	}
	
	public static CECServer getReference() {
		if (instance == null) {
			instance = new CECServer();
		}
		return instance;
	}
	
	public void add(Socket clientSocket, String emailAddress) {
		System.out.println("Put in Map: " + clientSocket.toString() + " " + emailAddress);
		clientSocketToEmailAddress.put(clientSocket, emailAddress);
	}
	
	public Socket getSocketForEmailAddress(String email) {
		Socket result = null;
		for(Map.Entry<Socket, String> entry: clientSocketToEmailAddress.entrySet()) {
			if (entry.getValue().equals(email))
				System.out.println("Sockets for email: " + entry.getValue().toString() + " is: " + entry.getKey().toString());
				result = entry.getKey();
		}
		
		if (result != null)
			return result;
		
		// none were found?
		throw new IllegalStateException("Did not find the socket you were looking for");
	}
	
    public static void main(String[] args) throws IOException {
        
    	
    	ServerSocket serverSocket = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(4445);
           // serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4445.");
        //    System.exit(-1);
        }

        while (listening)
        	new CECServerThread(serverSocket.accept()).start();

        Cleanup.closeQuietly(serverSocket);
        instance = null;
    }
}

package cec.net;
import java.net.*;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;

public class CECMultiServer {
	private static CECMultiServer instance;
	Map<String, String> clientIPToEmailAddress = new TreeMap<>();
	
	public static CECMultiServer getReference() {
		if (instance == null) {
			instance = new CECMultiServer();
		}
		return instance;
	}
	
	public synchronized void add(String email, String ip) {
		clientIPToEmailAddress.put(email, ip);
		System.out.println(((TreeMap<String, String>)clientIPToEmailAddress).size());
	}
	
	public synchronized String getHostNameForEmailAddress(String email) {
		return clientIPToEmailAddress.get(email);
	}
	
    public static void main(String[] args) throws IOException {
        
    	
    	ServerSocket serverSocket = null;
        boolean listening = true;
        try {
            serverSocket = new ServerSocket(4445);
           // serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
        //    System.exit(-1);
        }

        while (listening)
        	new CECMultiServerThread(serverSocket.accept()).start();

        serverSocket.close();
    }
}

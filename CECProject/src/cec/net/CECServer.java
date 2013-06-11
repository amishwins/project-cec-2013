package cec.net;
import java.net.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.io.*;

import cec.model.Email;

public class CECServer {
	private static CECServer instance;
	
	ConcurrentMap<String, Socket> clientEmailToCurrentSocket;
	ConcurrentMap<String, LinkedBlockingDeque<Object>> emailsForClient;
	
	ConcurrentMap<Socket, String> clientSocketToEmailAddress;
	
	private CECServer() {
		clientSocketToEmailAddress = new ConcurrentHashMap<>();
		clientEmailToCurrentSocket = new ConcurrentHashMap<>();
	}
	
	public static CECServer getReference() {
		if (instance == null) {
			instance = new CECServer();
		}
		return instance;
	}
	
	public void add(String emailAddress, Socket clientSocket) {
		System.out.println("Put in Map: " + clientSocket.toString() + " " + emailAddress);
		clientEmailToCurrentSocket.put(emailAddress, clientSocket);
	}
	
	public Socket getSocketForEmailAddress(String email) {
		System.out.println("Get from map: " + clientEmailToCurrentSocket.get(email).toString());
		return clientEmailToCurrentSocket.get(email);
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


        while (listening) {
        	new CECServerThread(serverSocket.accept()).start();
        }

        Cleanup.closeQuietly(serverSocket);
        instance = null;
    }
}

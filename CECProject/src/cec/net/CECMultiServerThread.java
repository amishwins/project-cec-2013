package cec.net;

import java.net.*;
import java.io.*;

public class CECMultiServerThread extends Thread {
    private Socket socket = null;
   

    public CECMultiServerThread(Socket socket) {
		super("CECMultiServerThread");
		this.socket = socket;
		
    }

    public void run() {
	
		try {
			CECMultiServer server = CECMultiServer.getReference();
		    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    socket.getInputStream()));
            String inputLine = in.readLine(), outputLine = null;
            String[] parsedEmailAddress = inputLine.split(" ");
	       	server.add(parsedEmailAddress[1],socket.getInetAddress().getHostName());
	    	
	    	System.out.println(parsedEmailAddress[1]);
	    	System.out.println(server.getHostNameForEmailAddress(parsedEmailAddress[1]));
            
	    	
		    while ((inputLine = in.readLine()) != null) {
     	       System.out.println(inputLine); 
		       
   	            

		    }
		    out.close();
		    in.close();
		    socket.close();
	
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}

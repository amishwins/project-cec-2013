package cec.net;

import java.net.*;
import java.io.*;

public class CECServerThread extends Thread {
	private Socket socket = null;

	public CECServerThread(Socket socket) {
		super("CECMultiServerThread");
		this.socket = socket;
	}
	
	

	public void run() {
	
		CECServer server = CECServer.getReference();
		try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String inputLine = in.readLine();
				String[] parsedEmailAddress = inputLine.split(" ");
				server.add(parsedEmailAddress[1], socket);
	
				while ((inputLine = in.readLine()) != null) {
					System.out.println(server.getSocketForEmailAddress(inputLine).toString());
				}
	
				Cleanup.closeQuietly(out);
				Cleanup.closeQuietly(in);
				Cleanup.closeQuietly(socket);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

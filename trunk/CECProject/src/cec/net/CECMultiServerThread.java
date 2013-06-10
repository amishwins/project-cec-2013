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
		
		CECMultiServer server = CECMultiServer.getReference();
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine = in.readLine();
			String[] parsedEmailAddress = inputLine.split(" ");
			server.add(socket, parsedEmailAddress[1]);

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

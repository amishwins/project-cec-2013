package cec.net;

import java.io.*;
import java.net.*;

public class CECClientSocket {

	public static void main(String[] args) throws IOException {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			kkSocket = new Socket("knowles.encs.concordia.ca", 4445);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: taranis.");
			System.exit(1);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to: taranis.");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		String fromServer;
		String fromUser;

		// - Introduce ourselves to the server
		// out.println("register: deyvid.william@gmail.com");

		while ((fromUser = stdIn.readLine()) != null) {
			
			if (fromUser != null) {
				// System.out.println("Client: " + fromUser);
				out.println(fromUser);
			}
		}

		Cleanup.closeQuietly(out);
		Cleanup.closeQuietly(in);
		Cleanup.closeQuietly(stdIn);
		Cleanup.closeQuietly(kkSocket);

		/*
		 * out.close(); in.close(); stdIn.close(); kkSocket.close();
		 */
	}
}

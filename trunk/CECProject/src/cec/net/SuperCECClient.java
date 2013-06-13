package cec.net;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.UUID;

import exceptions.StackTrace;

class SuperCECClient {
    PrintStream out;
    ObjectStream os;
    Socket writer;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    volatile boolean stop = false;

    SuperCECClient(String host, int port, PrintStream out){
        this.out = out;
      //  os = new ObjectStream(out);
        try {
            writer = new Socket(host, port);
            outputStream = new ObjectOutputStream( writer.getOutputStream() );
          //  inputStream = new ObjectInputStream( writer.getInputStream() ); 
           // outputStream2 = new ObjectOutputStream( writer.getOutputStream() );
         } catch (Exception e) {
            //cleanUpAll();
            out.println(StackTrace.asString(e));
        }
    }

    void stop(){
        stop = true;
    }

    private void cleanUpAll() {
        Cleanup.closeQuietly(outputStream);
        Cleanup.closeQuietly(writer);
    }
    
    public static void main (String args[]){
    	SuperCECClient client = new SuperCECClient("localhost",4444,System.out);
    	
     	HandShake hs = new HandShake();
        hs.email="pankajkapania@yahoo.com";
        try {
        	client.outputStream.writeObject(hs);
        	
        	Email email = new Email();
        	email.id = UUID.randomUUID();
        	email.to = "amish.gala@gmail.com"; 

        	//client.outputStream.writeObject(email);
        	
        	ObjectInputStream inputStream = new ObjectInputStream( client.writer.getInputStream() ); 
        	while(true){
        		Email e;
				try {
					e = (Email) inputStream.readObject();
					System.out.println(e.to);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		
        		
        		
        	}
   
        } catch (IOException e) {
        	client.out.println(StackTrace.asString(e));
        }
        client.cleanUpAll();
    }
}

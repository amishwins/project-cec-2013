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
    
    public void handShake() throws IOException {

     	HandShake hs = new HandShake();
        hs.emailAddress = "pankajkapania@yahoo.com";
        outputStream.writeObject(hs);
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
    	
        try {
        	client.handShake();
        	
        	EmailTest email = new EmailTest();
        	email.id = UUID.randomUUID();
        	email.to = "amish.gala@gmail.com"; 

        	//client.outputStream.writeObject(email);
        	
        	ObjectInputStream inputStream = new ObjectInputStream( client.writer.getInputStream() ); 
        	while(true){
        		EmailTest e;
				try {
					e = (EmailTest) inputStream.readObject();
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

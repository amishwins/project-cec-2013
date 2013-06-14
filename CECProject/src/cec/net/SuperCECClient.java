package cec.net;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import cec.config.CECConfigurator;
import cec.model.Email;
import cec.model.EmailBuilder;
import cec.model.FolderFactory;
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
    	SuperCECClient client = new SuperCECClient("localhost",7777,System.out);
    	
        try {
        	client.handShake();
        	EmailBuilder emailBuilder = new EmailBuilder();
    		Email email = emailBuilder.computeID()
    				.withFrom("pankajkapania@gmail.com")
    				.withTo("romeo@cec.com")
    				.withCC("PankajKapania@gmail.com")
    				.withSubject("TestSubject1")
    				.withBody("Body1")
    				.withLastModifiedTime("2013.05.12_At_14.07.56.874")
    				.withSentTime("2013.05.13_At_14.07.56.874")
    				.withParentFolder(FolderFactory.getFolder(CECConfigurator.getReference().get("Inbox")))
    				.build();
    		Email email2 = emailBuilder.computeID()
    				.withFrom("pankajkapania@gmail.com")
    				.withTo("deyvid@cec.com")
    				.withCC("PankajKapania@gmail.com")
    				.withSubject("TestSubject1")
    				.withBody("Body1")
    				.withLastModifiedTime("2013.05.12_At_14.07.56.874")
    				.withSentTime("2013.05.13_At_14.07.56.874")
    				.withParentFolder(FolderFactory.getFolder(CECConfigurator.getReference().get("Inbox")))
    				.build();

        	client.outputStream.writeObject(email);
        	client.outputStream.writeObject(email2);
        	
        	ObjectInputStream inputStream = new ObjectInputStream( client.writer.getInputStream() ); 
        	while(true){
        		/*EmailTest e;
				try {
					e = (EmailTest) inputStream.readObject();
					System.out.println(e.to);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		
        		
        		*/
        	}
   
        } catch (IOException e) {
        	client.out.println(StackTrace.asString(e));
        }
        client.cleanUpAll();
    }
}

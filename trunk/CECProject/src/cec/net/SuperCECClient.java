package cec.net;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

import exceptions.StackTrace;

class SuperCECClient {
    PrintStream out;
    ObjectStream os;
    Socket writer;
    ObjectOutputStream outputStream;
    volatile boolean stop = false;

    SuperCECClient(String host, int port, PrintStream out){
        this.out = out;
      //  os = new ObjectStream(out);
        try {
            writer = new Socket(host, port);
            outputStream = new ObjectOutputStream( writer.getOutputStream() );
            Thread writerThread = new Thread( new WriteToSocket());
            writerThread.start();
        } catch (IOException e) {
            cleanUpAll();
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
    }

    class WriteToSocket implements Runnable {
        public void run() {
            while(!stop){
               	HandShake hs = new HandShake();
                hs.email="Pankaj@yahoo.com";
                try {
                    outputStream.writeObject(hs);
                   // out.println("sent: " + hs);
                    outputStream.flush();
                    Wait.seconds(1);
                } catch (IOException e) {
                    out.println(StackTrace.asString(e));
                }
            }
            cleanUpAll();
        }
    }
}

package cec.net.temporary;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class SocketReader {
    ServerSocket serverSocket;
    Socket reader;
    PrintStream out;
    volatile boolean stop = false;
    byte[] byteBuffer =  new byte[1024];
    ObjectStream os;
    ObjectInputStream in;
    String host;
    int port;

    SocketReader(String host, int port, PrintStream out){
        this.host = host;
        this.port = port;
        this.out = out;
        os = new ObjectStream(out);
        Thread connectionListener = new Thread( new ListenConnection() );
        connectionListener.start();
    }

    private void cleanUpAll() {
        Cleanup.closeQuietly(in);
        Cleanup.closeQuietly(reader);
    }

    void stop(){
        stop = true;
    }

    class ListenConnection implements Runnable {
        public void run() {
            try {
                serverSocket = new ServerSocket(port, 0, InetAddress.getByName(host));
                reader = serverSocket.accept();
                in = new ObjectInputStream( reader.getInputStream() );
                Thread readThread = new Thread( new ReadSocket() );
                readThread.start();
            } catch (IOException e) {
                cleanUpAll();
                out.println(StackTrace.asString(e));
            }

        }
    }

    class ReadSocket implements Runnable {
        public void run() {
            while(!stop){
                try {
                    Message message = (Message)in.readObject();
                    out.println("received: " + message);
                } catch (Exception e) {
                    out.println(StackTrace.asString(e));
                }
            }
            cleanUpAll();
        }
    }
}

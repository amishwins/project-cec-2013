package cec.net.temporary;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;

class SocketWriter {
    PrintStream out;
    ObjectStream os;
    Socket writer;
    ObjectOutputStream outputStream;
    volatile boolean stop = false;

    SocketWriter(String host, int port, PrintStream out){
        this.out = out;
        os = new ObjectStream(out);
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

    class WriteToSocket implements Runnable {
        public void run() {
            while(!stop){
                Message ping = new Message("ping");
                try {
                    outputStream.writeObject(ping);
                    out.println("sent: " + ping);
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

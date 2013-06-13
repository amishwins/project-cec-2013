package cec.net.temporary;
import java.io.PrintStream;

public class SocketExample {
    static PrintStream out;
    static String host;

    public static void main(String[] args) {
        host = "localhost";
        out = System.out;

        SocketReader reader
                = new SocketReader(host, 7777, out);
        SocketWriter writer
                = new SocketWriter(host, 7777, out);
        Wait.seconds(7);
        writer.stop();
        reader.stop();
    }
}

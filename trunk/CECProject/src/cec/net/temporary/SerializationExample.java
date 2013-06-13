package cec.net.temporary;
import java.io.PrintStream;

public class SerializationExample extends ConsoleApplet {
    protected void run() {
        PrintStream out = console.out;
        ObjectStream os = new ObjectStream(out);

        Message message = new Message("I am going to be turned into bytes");
        out.println("message was: " + message);

        byte[] messageBytes = os.serialize(message);

        Message rebuiltMessage = os.deserialize(messageBytes);

        out.println("message rebuilt: " + message);
    }
}

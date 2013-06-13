package cec.net.temporary;
import java.io.PrintStream;
import java.util.Arrays;

public class ExternalizableExample extends ConsoleApplet {
    protected void run() {
        PrintStream out = console.out;
        ObjectStream os = new ObjectStream(out);
        ExternalizableList list
                = new ExternalizableList(Arrays.asList(1, 2, 3));

        out.println( "original: " + list );

        byte[] listBytes = os.serialize(list);

        ExternalizableList rebuilt = os.deserialize(listBytes);

        out.println("rebuilt: " + rebuilt);
    }
}

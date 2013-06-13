package cec.net.temporary;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Message implements Serializable {
    transient static AtomicInteger idGenerator = new AtomicInteger();

    int id;
    String contents;

    public void setId(int id) {
        this.id = id;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public Message(String contents) {
        id = idGenerator.getAndIncrement();
        this.contents = contents;
    }

    public String toString() {
        return "" + id + ": " + contents;
    }
}

package cec.net.temporary;
import java.io.Serializable;


public class Ack implements Serializable{
    static final long serialVersionUID = 42L;
    int messageReceived;

    public Ack(int messageReceived) {
        this.messageReceived = messageReceived;
    }

    public int getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(int messageReceived) {
        this.messageReceived = messageReceived;
    }
}

package cec.net.temporary;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class EnforceInvariants implements Serializable {
    int amount;

    // must have default public no arg
    // constructor for serialization to work
    public EnforceInvariants(){

    }

    public EnforceInvariants(int amount) {
        checkPositive(amount);
        this.amount = amount;
    }

    private void checkPositive(int amount) {
        if (1 < amount) {
            throw new IllegalStateException("amount must be > 1");
        }
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        checkPositive(amount);
        this.amount = amount;
    }

    // override this to enforce your invariants
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException,
                                                          IOException {
        ois.defaultReadObject();
        checkPositive(amount);
    }
}

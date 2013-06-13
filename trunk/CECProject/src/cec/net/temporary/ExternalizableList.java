package cec.net.temporary;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.LinkedList;
import java.util.List;

public class ExternalizableList implements Externalizable {
    List<Integer> ints;

    // still need default constrcutor for serialization
    public ExternalizableList() {
        ints = new LinkedList<Integer>();
    }

    public ExternalizableList(List<Integer> ints) {
        this.ints = new LinkedList<Integer>(ints);
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt( ints.size() );
        for(int i : ints){
            out.writeInt(i);
        }
    }

    public void readExternal(ObjectInput in) throws IOException,
                                                    ClassNotFoundException {
        int size = in.readInt();
        for(int i = 0; i < size; i++){
            ints.add( in.readInt());
        }
    }

    public String toString() {
        return ints.toString();
    }
}

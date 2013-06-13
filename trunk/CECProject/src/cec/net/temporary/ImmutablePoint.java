package cec.net.temporary;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class ImmutablePoint implements Serializable {
    static final long serialVersionUID = 42L;
    final int x;
    final int y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private Object writeReplace(){
        return  new SerializationProxy(this);
    }

    // prevent serialization attack
    private Object readObject(ObjectInputStream in)
            throws InvalidObjectException {
        throw new InvalidObjectException("Use proxy");
    }

    private static class SerializationProxy implements Serializable {
        int sx, sy;

        public SerializationProxy(ImmutablePoint p){
            sx = p.x;
            sy = p.y;
        }

        Object readResolve(){
            return new ImmutablePoint(sx, sy);
        }
    }
}

package cec.net;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import cec.exceptions.StackTrace;


public class ObjectStream {
    PrintStream out;

    public ObjectStream(PrintStream out){
        this.out = out;
	}
	
	public <T extends Serializable> byte[] serialize(T serializable){
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		
		try{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream( baos );
			oos.writeObject(serializable);
		}
		catch( IOException e ){
			out.println(StackTrace.asString(e));
		}
		finally{
			Cleanup.closeQuietly(oos);
			Cleanup.closeQuietly(baos);
		}
		
		return baos.toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T deserialize( byte[] data ){
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		T t = null;
		
		try{
			bais = new ByteArrayInputStream( data );
			ois = new ObjectInputStream( bais );
			t = (T)ois.readObject();
		}
		catch( Exception e ){
            out.println(StackTrace.asString(e));
		}
		finally{
			Cleanup.closeQuietly(ois);
			Cleanup.closeQuietly(bais);
		}
		return t;
	}
}

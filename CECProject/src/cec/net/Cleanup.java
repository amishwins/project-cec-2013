package cec.net;

/**
 * Class Cleanup used to close objects in the Network Layer.  
 */

import java.io.Closeable;
import java.lang.reflect.Method;

public class Cleanup {
	private Cleanup(){
	}

	public static void closeQuietly(Closeable closeable) {
		if ( null == closeable ) return;
		try{
			closeable.close();
		} catch( Exception e){
			// explicitly silence exception
		}
	}

    public static void closeQuietly(Object closeable) {
		if ( null == closeable ) return;
        try{
            Class<?>[] params = new Class<?>[0];
            Method closeMethod = closeable.getClass().getMethod("close", params);
			Object[] args = new Object[0];
            closeMethod.invoke(closeable,args);
		} catch( Exception e){
			
		}
	}
	

}

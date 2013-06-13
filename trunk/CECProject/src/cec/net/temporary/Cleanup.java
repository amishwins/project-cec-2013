package cec.net.temporary;
//import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

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
			// explicitly silence exception
		}
	}
	

}

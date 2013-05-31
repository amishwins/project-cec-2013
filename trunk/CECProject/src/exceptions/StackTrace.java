package exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class StackTrace {
    public static String asString(Throwable toConvert) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        toConvert.printStackTrace(printWriter);
        return result.toString();
    }
}

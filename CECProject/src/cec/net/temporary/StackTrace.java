package cec.net.temporary;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

class StackTrace {
    static String asString(Throwable toConvert) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        toConvert.printStackTrace(printWriter);
        return result.toString();
    }
}

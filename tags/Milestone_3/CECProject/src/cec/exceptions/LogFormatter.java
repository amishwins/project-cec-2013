package cec.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class LogFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record) {
        StringBuilder stringBuilder = new StringBuilder();
        Date date = new Date(record.getMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");  
             stringBuilder.append(sdf.format(date))
            .append(" ")
            .append(record.getLevel().getLocalizedName())
            .append(": ")
            .append(formatMessage(record))
            .append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
            try {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                record.getThrown().printStackTrace(printWriter);
                printWriter.close();
                stringBuilder.append(stringWriter.toString());
            } catch (Exception ex) {
               
            }
        }

        return stringBuilder.toString();
    }
}


package cec.net.temporary;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Console extends JPanel {
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    PipedInputStream in2Out = new PipedInputStream();
    PipedOutputStream pout;
    PrintStream out;
    BufferedReader bout;
    // here I got it right
    volatile boolean read = false;
    Thread reader;

    public Console(int width, int height) {
        try {
            pout = new PipedOutputStream(in2Out);
            out = new PrintStream(pout);
            bout = new BufferedReader( new InputStreamReader(in2Out) );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        Dimension preferredSize = new Dimension(width, height);
        reallySetSize(this, preferredSize);
        Dimension preferredTextSize = new Dimension((int)(width * 0.9), (int)(height * 0.9));
        reallySetSize(textArea, preferredSize);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setLayout( new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void reallySetSize(Component component, Dimension dimension){
        component.setSize(dimension);
        component.setMaximumSize(dimension);
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
    }

    public PrintStream outPrintStream(){
        return out;
    }

    public void start(){
        read = true;
        reader = new Thread( new StreamReader() );
        reader.start();
    }

    public void stop(){
        read = false;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // swallow this
        }
        closeQuietly(bout);
        closeQuietly(out);
        closeQuietly(in2Out);
    }

    private void closeQuietly(Closeable closeable) {
        if ( null == closeable )
            return;
        try{
          closeable.close();
        } catch(IOException e){
            // do nothing
        }
    }

    class TextWriter implements Runnable {
        String line;

        TextWriter(String line){
            this.line = line;
        }

        public void run() {
            textArea.append(line);
            textArea.append("\n");
        }
    }

    class StreamReader implements Runnable {
        public void run() {
            while( read ){
                String line;
                try {
                    while( (line = bout.readLine() ) != null){
                        // thread confinement
                        SwingUtilities.invokeLater(
                                new TextWriter(line)
                        );
                    }
                } catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

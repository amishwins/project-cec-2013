package cec.net.temporary;
import javax.swing.*;
import java.awt.*;

public abstract class ConsoleApplet extends JApplet {
    Console console;
    int width;
    int height;

    public ConsoleApplet(){
        super();
    }


    public void init() {
        width = (int)(getWidth() * 0.8);
        height = (int)(getWidth() *0.8);
        layoutApplet();
    }

    private void layoutApplet() {
        console = new Console(width, height);
        setLayout( new BorderLayout());
        add(console, BorderLayout.CENTER);
    }

    // template method pattern
    protected abstract void run();

    public void start() {
        console.start();
        run(); // template method pattern
    }

    public void stop() {
        super.stop();
        console.stop();
    }

    public void destroy() {
        super.destroy();
        console.stop();
    }
}

package cec.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Collaborative Email Client Application's Entry Point
 * 
 * Executes a new runnable on AWT Event thread - necessary because 
 * most Swing object methods are not "thread safe". 
 * According to Oracle documentation, SWING programs which ignore this rule
 * are subject to unpredictable errors difficult to reproduce.
 * Source: http://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
 */

public class Main {
    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame mainWindow = EmailClient.getReference();
                mainWindow.setVisible(true);
            }
            
        });
    }
}

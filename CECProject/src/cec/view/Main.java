package cec.view;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

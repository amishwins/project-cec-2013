/**
 * @author Deyvid William / Created 07-May-13
 */

package cec.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                JFrame mainWindow = new EmailClient("Collaborative Email Client");
                mainWindow.setVisible(true);

            }
        });


    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import javax.swing.*;

public class NewMessage2 extends JFrame{
    
      public NewMessage2(String title){
  
        //Layout Manager
        super(title);
        setSize(670, 300);
        setLayout(new BorderLayout());
        setVisible(true);
        
        
        //Panels Hierarchy
            //JToolBar     (TOP)
            //JPanel       (MIDDLE) 
            //SrollPanel   (BOTTOM)
        
        
        //Swing Components - Top Panel 
        JButton send = new JButton(" Send >> ");
        JButton draft = new JButton("Save as Draft");
        JToolBar bar = new JToolBar();  
        bar.add(send);
        bar.add(draft);

        
        //Swing Components - Middle Panel
        JLabel to = new JLabel("To:         ");
        JTextField toField = new JTextField(22);
        JLabel cc = new JLabel("Cc:         ");
        JTextField ccField = new JTextField(22);
        JLabel subject = new JLabel("Subject:");
        JTextField subjectField = new JTextField(22);
        
        JPanel mid = new JPanel();  
        mid.setPreferredSize(new Dimension(320,300));     
        mid.add(to);
        mid.add(toField);
        mid.add(cc);
        mid.add(ccField);
        mid.add(subject);
        mid.add(subjectField);                      

        
        //Swing Components - Bottom Panel 
        JTextArea body = new JTextArea(10,10);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(body);
       
        
        //Finishing Panels Disposal
        add(bar, BorderLayout.NORTH);    
        add(mid, BorderLayout.LINE_START);  
        add(scroll);   
        
      }    
}

/*
 * NewMessage screen
  */

package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.*;
import service.NewMessageService;


public class NewMessage2 extends JFrame{
    
      public NewMessage2(){
  
        //Layout Manager
        super("New Message");
        setSize(610, 420);
        setLayout(new BorderLayout());
        setResizable(false);
        setVisible(true);  
        setLocationRelativeTo(null);

        
        //Panels Hierarchy
            //JToolBar     (TOP)
            //JPanel       (MIDDLE) 
            //SrollPanel   (BOTTOM)
        
        
        //Swing Components - Top Panel 
        ImageIcon sendIcon = new ImageIcon("images/email_send.png");
        ImageIcon draftIcon = new ImageIcon("images/email_draft.png");
        ImageIcon discardIcon = new ImageIcon("images/email_discard.png");
        JButton send = new JButton(" Send >>  ",sendIcon );
        JButton draft = new JButton("Save as Draft ",draftIcon);
        JButton discard = new JButton("Discard ",discardIcon);
        JToolBar bar = new JToolBar();  
        bar.setFloatable(false);
        bar.add(send);
        bar.add(draft);
        bar.add(discard);
        
        
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendNewMessage(evt);}});
        
        draft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                draftNewMessage(evt);}});
        
        discard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discardNewMessage(evt);}});
        


        
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
        JTextArea body = new JTextArea(15,20);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(body);
       
        
        //Finishing Panels Disposal
        add(bar, BorderLayout.NORTH);    
        add(mid, BorderLayout.LINE_START);  
        add(scroll, BorderLayout.SOUTH);   
                
        
      }    
      
      //Actions - Discard
      private void discardNewMessage(ActionEvent evt) {                                
        this.dispose();        
    }                                        
      
      //Actions - Discard
      private void draftNewMessage(ActionEvent evt) {   
       
       /* String to = toField.getText();
        String cc = ccField.getText();
        String subject = jTextField3.getText();
        String body = jTextArea1.getText();
        newEmailService.draftEmail(to, cc, subject, body);  */
    }                                        
      
      //Actions - Discard
      private void sendNewMessage(ActionEvent evt) {                                
      /*  String to = jTextField1.getText();
        String cc = jTextField2.getText();
        String subject = jTextField3.getText();
        String body = jTextArea1.getText();
        newEmailService.sendEmail(to, cc, subject, body);   */    
    }                                        
            
}




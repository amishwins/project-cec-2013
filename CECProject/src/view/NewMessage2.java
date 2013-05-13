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
    
      NewMessageService newEmailService = new NewMessageService(); 
    
      //Shared Components
      JTextField toField = new JTextField(22);
      JTextField ccField = new JTextField(22);
      JTextField subjectField = new JTextField(22);
      JTextArea bodyField = new JTextArea(15,20);
    
    
      public NewMessage2(){
  
        //Layout Manager
        super("New Message");
        setSize(610, 440);
        setLayout(new BorderLayout());
        setResizable(false);
        setVisible(true);  
        setLocationRelativeTo(null);

        
        //Menu
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenuBarEntry = new JMenu("File");
        menuBar.add(fileMenuBarEntry);    
        
        JMenuItem sendItem = new JMenuItem("Send");
        fileMenuBarEntry.add(sendItem);   
        
        JMenuItem draftItem = new JMenuItem("Save as Draft");
        fileMenuBarEntry.add(draftItem);  

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenuBarEntry.add(exitItem);  
                
 
        sendItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendNewMessage();}});
        
        draftItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                draftNewMessage();}});
        
        exitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discardNewMessage();}});        
                
        
        
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
                sendNewMessage();}});
        
        draft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                draftNewMessage();}});
        
        discard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discardNewMessage();}});
        

        
        //Swing Components - Middle Panel
        JLabel to = new JLabel("To:         ");       
        JLabel cc = new JLabel("Cc:         ");        
        JLabel subject = new JLabel("Subject:");        
        
                
        JPanel mid = new JPanel();  
        mid.setPreferredSize(new Dimension(320,300));     
        mid.add(to);
        mid.add(toField);
        mid.add(cc);
        mid.add(ccField);
        mid.add(subject);
        mid.add(subjectField);                      

        
        //Swing Components - Bottom Panel 
        bodyField.setLineWrap(true);
        bodyField.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(bodyField);
       
        
        //Finishing Panels Disposal
        add(bar, BorderLayout.NORTH);    
        add(mid, BorderLayout.LINE_START);  
        add(scroll, BorderLayout.SOUTH);   
                
        
      }    
      
      //Actions - Discard
      private void discardNewMessage() {                                
        this.dispose();        
    }                                        
      
      //Actions - Draft
      private void draftNewMessage() {   
       
        String auxTo = toField.getText();
        String auxCc = ccField.getText();
        String auxSubject = subjectField.getText();
        String auxBody = bodyField.getText();
        newEmailService.draftEmail(auxTo, auxCc, auxSubject, auxBody);  
    }                                        
      
      //Actions - Send
      private void sendNewMessage() {                                
        String auxTo = toField.getText();
        String auxCc = ccField.getText();
        String auxSubject = subjectField.getText();
        String auxBody = bodyField.getText();
        newEmailService.sendEmail(auxTo, auxCc, auxSubject, auxBody);    
    }                                        
            
}




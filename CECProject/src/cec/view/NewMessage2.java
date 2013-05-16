/*
 * NewMessage screen
  */

package cec.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;

import cec.service.NewMessageService;


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
        
        JMenuItem sendItem = new JMenuItem("Send",KeyEvent.VK_S);
        sendItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK));
        fileMenuBarEntry.add(sendItem);   
        
        JMenuItem draftItem = new JMenuItem("Save as Draft",KeyEvent.VK_D);
        draftItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,InputEvent.CTRL_DOWN_MASK));
        fileMenuBarEntry.add(draftItem);  

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("ESCAPE"));
        
        
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
        //bodyField.addKeyListener(KenterL);        
      } 
      
      
     /* java.awt.event.KeyListener KenterL = new java.awt.event.KeyListener()
        {			
                public void keyPressed(java.awt.event.KeyEvent ke) 
                {				
                        if(ke.isControlDown())
                        {	
                                if(ke.getKeyCode()==ke.VK_S)
                                { 
                                        sendNewMessage();
                                        JOptionPane.showMessageDialog(null, "MESSAGE SENT");
                                }

                                else if (ke.getKeyCode()==ke.VK_D)
                                {						
                                        draftNewMessage();
                                        JOptionPane.showMessageDialog(null, "MESSAGE SENT TO DRAFT");
                                }
                                else if (ke.getKeyCode()==ke.VK_V)
                                {			
                                        JOptionPane.showMessageDialog(null, "V   ESCAPE");
                                }

                        }

                        if(ke.getKeyCode()==ke.VK_ESCAPE)
                        {
                                discardNewMessage();
                                JOptionPane.showMessageDialog(null, "DISCARD");
                        }

                }	


                @Override
                public void keyReleased(java.awt.event.KeyEvent ke) {
                        // TODO Auto-generated method stub

                }

                @Override
                public void keyTyped(java.awt.event.KeyEvent ke) {
                        // TODO Auto-generated method stub

                }				
        };*/
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




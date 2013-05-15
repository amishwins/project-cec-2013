/**
 * @author Deyvid William / Created 07-May-13
 */

package model;

import persistence.NewMessageDao;
import persistence.NewMessageDaoFactory;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class EmailImpl implements Email {
    
    private String to;
    private String subject;
    private String cc;
    private String body;
    private String lastAccessedTime;
    private String sentTime;    
    private String folder;
    private UUID id;
    private NewMessageDao newMessageDao;
    
    
    public EmailImpl (String to, String cc, String subject, String body){
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        //this.lastAccessedTime= currentDateTime();
    }
    protected EmailImpl(String to, String cc, String subject, String body, String lastAccessedTime){
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
        this.lastAccessedTime = lastAccessedTime;
    }
    
    public void setTo(String to){
        this.to = to;
    }
    public void setSubject(String subject){
        this.subject=subject;
    }
    public void setbody(String body){
        this.body=body;
    }
    public void setCC(String to){
        this.to = to;
    }
    
    public String getTo(){
        return to;
    }
      public String getCC(){
        return to;
    }
    public String getSubject(){
        return subject;
    }
    public String getBody(){
        return body;
    }
    public String getLastAccessedTime(){
        return lastAccessedTime;
    }
    protected void setLastAccessedTime(String lastAccessedTime){
        this.lastAccessedTime = lastAccessedTime;
    }
    
    private String currentDateTime(){
        SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy.MM.dd_'At'_HH.mm.ss.SSS");
	return currentDateTime.format(new Date());
    }
    public void send(){
        newMessageDao = NewMessageDaoFactory.getNewMessageDao();
        //Assuming Email has been sent successfully.
        newMessageDao.save(to, cc, subject, body, currentDateTime(), "Sent");
    }
    public void saveToDraftFolder(){
        newMessageDao = NewMessageDaoFactory.getNewMessageDao();
        newMessageDao.save(to, cc, subject, body, currentDateTime(), "Draft");
    }
    
}

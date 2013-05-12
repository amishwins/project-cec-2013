/**
 * @author Deyvid William / Created 07-May-13
 */

package model;

import persistence.NewMessageDao;
import persistence.NewMessageDaoFactory;
public class EmailImpl implements Email {
    
    //Class atributes - Private Variables
    private String to;
    private String subject;
    private String cc;
    private String body;
    private String folder;
    private NewMessageDao newMessageDao;
    
    
    public EmailImpl (String to, String cc, String subject, String body){
        this.to = to;
        this.cc = cc;
        this.subject = subject;
        this.body = body;
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
    
    public String getTo(){
        return to;
    }
    public String getSubject(){
        return subject;
    }
    public String getBody(){
        return body;
    }
    public void send(){
        newMessageDao = NewMessageDaoFactory.getNewMessageDao();
        //Assuming Email has been sent successfully.
        newMessageDao.save(to, cc, subject, body, "Sent");
    }
    public void saveToDraftFolder(){
        newMessageDao = NewMessageDaoFactory.getNewMessageDao();
        //Assuming Email has been sent successfully.
        newMessageDao.save(to, cc, subject, body, "Draft");
    }
    
}

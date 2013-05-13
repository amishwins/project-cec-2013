/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Pankaj Kapania
 */
public class EmailBuilder {
    String to = "";
    String cc = "";
    String subject = "";
    String body = "";
    String lastAccessedTime="";

    // method chaining
    public EmailBuilder withTo(String to){
        this.to = to;
        return this;
    }

    public EmailBuilder withCC(String cc){
        this.cc = cc;
        return this;
    }

    public EmailBuilder withSubject(String subject){
        this.subject = subject;
        return this;
    }

    public EmailBuilder withBody(String body){
        this.body = body;
        return this;
    }
    protected EmailBuilder withLastAccessedTime(String lastAccessedTime){
        this.lastAccessedTime = lastAccessedTime;
        return this;
    }
    public Email build(){
        return new EmailImpl(to, cc, subject, body);
    }
    
    protected Email buildWithAccessedTime(){
        return new EmailImpl(to, cc, subject, body,lastAccessedTime);
    }
}
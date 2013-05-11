/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Pankaj Kapania
 */
public class EMailBuilder {
    String to = "";
    String cc = "";
    String subject = "";
    String body = "";

    // method chaining
    public EMailBuilder withTo(String to){
        this.to = to;
        return this;
    }

    public EMailBuilder withCC(String cc){
        this.cc = cc;
        return this;
    }

    public EMailBuilder withSubject(String subject){
        this.subject = subject;
        return this;
    }

    public EMailBuilder withBody(String body){
        this.body = body;
        return this;
    }

    public Email build(){
        return new Email(to, cc, subject, body);
    }
}

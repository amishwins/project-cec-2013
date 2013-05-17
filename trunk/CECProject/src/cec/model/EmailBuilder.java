package cec.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cec.config.CECConfigurator;

/**
 *
 * @author Pankaj Kapania
 */
public class EmailBuilder {

    UUID id;
    String from = "";
    String to = "";
    String cc = "";
    String subject = "";
    String body = "";
    String lastModifiedTime = "";
    String sentTime = "";
    Folder parentFolder;

    // method chaining
    public EmailBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public EmailBuilder withFrom(String from) {
        this.from = from;
        return this;
    }

    public EmailBuilder withTo(String to) {
        this.to = to;
        return this;
    }

    public EmailBuilder withCC(String cc) {
        this.cc = cc;
        return this;
    }

    public EmailBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailBuilder withBody(String body) {
        this.body = body;
        return this;
    }

    public EmailBuilder withLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
        return this;
    }

    public EmailBuilder withSentTime(String sentTime) {
        this.sentTime = sentTime;
        return this;
    }

    public EmailBuilder withParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
        return this;
    }

    public EmailBuilder computeID() {
        this.id = UUID.randomUUID();
        return this;
    }

    public EmailBuilder computelastModifiedTime() {
        this.lastModifiedTime = currentDateTime();
        return this;
    }

    public EmailBuilder computeSentTime() {
        this.sentTime = currentDateTime();
        return this;
    }

    private String currentDateTime() {
        SimpleDateFormat currentDateTime = new SimpleDateFormat(
        		CECConfigurator.getReference().get("DateFormat"));
        return currentDateTime.format(new Date());
    }

    public Email build() {
        return new EmailImpl(id, from, to, cc, subject, body, lastModifiedTime, sentTime, parentFolder);
    }
}

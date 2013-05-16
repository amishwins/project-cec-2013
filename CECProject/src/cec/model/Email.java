/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import java.util.UUID;

/**
 *
 * @author Pankaj Kapania and the team
 */
public interface Email {
    public void send();
    public void saveToDraftFolder();
    
    public UUID getId();
    public String getTo();
    public String getCC();
    public String getSubject();
    public String getBody();
    public String getSentTime();
    public String getLastModifiedTime();
    public Folder getParentFolder();
}

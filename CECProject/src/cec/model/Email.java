/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.model;

import java.util.UUID;

/**
 * @author a_gala
 *
 */
public interface Email extends Comparable<Email> {
    public void send();
    public void saveToDraftFolder();
    public void delete();
    public void move(Folder destDir);
    
    public UUID getId();
    public String getFrom();
    public String getTo();
    public String getCC();
    public String getSubject();
    public String getBody();
    public String getSentTime();
    public String getLastModifiedTime();
    public Folder getParentFolder();
	public String getLastModifiedTimeNicelyFormatted();
}

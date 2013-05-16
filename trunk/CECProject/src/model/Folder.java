/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Pankaj Kapania
 */
public interface Folder {
	public String getName();
	public String getPath();
    public Iterable<Email> loadEmails(String folder);
}

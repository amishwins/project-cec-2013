/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistence;

/**
 *
 * @author Pankaj Kapania
 */
public interface NewMessageDao {
    public void save(String to, String subject, String body, String location);
}

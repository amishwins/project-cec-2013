/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

/**
 *
 * @author Pankaj Kapania
 */
public interface NewMessageDao {
    public void save(String to, String cc, String subject, String body, String location);
}

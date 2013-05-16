/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.persistence;

/**
 *
 * @author Pankaj Kapania
 */
public interface EmailDao {
    public void save(String to, String cc, String subject, String body,String lastAccessedTime, String location);
}

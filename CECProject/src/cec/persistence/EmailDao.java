/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.persistence;

import java.util.UUID;

/**
 *
 * @author Pankaj Kapania
 */
public interface EmailDao {
    public void save(UUID id, String from,  String to, String cc, String subject, String body,String lastModifiedTime,String sentTime, String location);
}

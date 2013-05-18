/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cec.persistence;

/**
 *
 * @author Pankaj Kapania
 */
public class EmailDaoFactory {
    public static EmailDao getNewMessageDao() {
        return new EmailXMLDao();
    }
}

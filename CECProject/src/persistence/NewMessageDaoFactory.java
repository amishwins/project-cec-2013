/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistence;

/**
 *
 * @author Pankaj Kapania
 */
public class NewMessageDaoFactory {
    public static NewMessageDao getNewMessageDao() {
        return new NewMessageXMLDao();
    }
}

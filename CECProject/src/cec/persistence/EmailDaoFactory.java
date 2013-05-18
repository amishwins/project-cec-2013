package cec.persistence;

public class EmailDaoFactory {
    public static EmailDao getEmailDaoInstance() {
        return new EmailXMLDao();
    }
}

package cec.persistence;

public class FolderDaoFactory {
    public static FolderDao getFolderDaoInstance() {
        return new FolderDaoImpl();
    }
}

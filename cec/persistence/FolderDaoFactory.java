package cec.persistence;

/**
 * A factory for creating FolderDao objects.
 */
public class FolderDaoFactory {
    
    /**
     * It returns the instance of class implementing 
     * the FolderDao Interface.
     *
     * @return the folder dao instance
     */
    public static FolderDao getFolderDaoInstance() {
        return new FolderDaoImpl();
    }
}

package exceptions;

public class SourceAndDestinationFoldersAreSameException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SourceAndDestinationFoldersAreSameException (){
		super("Source and DestinationFolders are Same!");
	}
	
    public SourceAndDestinationFoldersAreSameException (String messege){
    	super(messege);
	}
}

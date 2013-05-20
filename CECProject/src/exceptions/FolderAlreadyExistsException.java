package exceptions;

public class FolderAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FolderAlreadyExistsException (){
		super("FolderAlreadyExistsException");
	}
	
    public FolderAlreadyExistsException (String messege){
    	super(messege);
	}
	
	
}

package cec.exceptions;

public class FolderAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FolderAlreadyExistsException (){
		super("Folder Already Exists !");
	}
	
    public FolderAlreadyExistsException (String messege){
    	super(messege);
	}
	
	
}

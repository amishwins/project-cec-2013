package exceptions;

public class CannotDeleteSystemFolderException extends RuntimeException {
	private static final long serialVersionUID = 6196097603607327382L;
	
	public CannotDeleteSystemFolderException (){
		super("Cannot Delete System Folder!");
	}
	
    public CannotDeleteSystemFolderException (String messege){
    	super(messege);
	}


}

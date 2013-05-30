package exceptions;

public class RootFolderSubfolderCreationException extends RuntimeException {

	private static final long serialVersionUID = -529431082654759631L;

	public RootFolderSubfolderCreationException (){
		super("CannotDeleteSystemFolderException");
	}
	
    public RootFolderSubfolderCreationException (String messege){
    	super(messege);
	}
	
}

package exceptions;

public class CannotDeleteSystemFolderException extends RuntimeException {
	private static final long serialVersionUID = 6196097603607327382L;
	
	public CannotDeleteSystemFolderException (){
		super("CannotDeleteSystemFolderException");
	}
	
    public CannotDeleteSystemFolderException (String messege){
    	super(messege);
	}


}

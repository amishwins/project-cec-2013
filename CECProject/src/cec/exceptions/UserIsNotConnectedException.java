package cec.exceptions;

public class UserIsNotConnectedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserIsNotConnectedException (){
		super("User is not connected to the CEC server!");
	}
	
    public UserIsNotConnectedException (String message) {
    	super(message);
	}
}

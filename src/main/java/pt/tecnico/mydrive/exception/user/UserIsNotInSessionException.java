package pt.tecnico.mydrive.exception.user;

import pt.tecnico.mydrive.exception.MyDriveException;


public class UserIsNotInSessionException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String username;

	
	public UserIsNotInSessionException(String username) {
		this.username = username;
	}

	
	public String getUsername() { return username; }

	@Override
	public String getMessage() {
		return "The User " + getUsername() + " is not in session.";
	}
	
}

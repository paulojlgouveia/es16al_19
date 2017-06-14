package pt.tecnico.mydrive.exception.user.association;

import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class UserAssociationException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String username;
	
	
	public UserAssociationException(String username) {
		this.username = username;
	}
	
	
	public String getUsername() { return username; }
	
	
	@Override
	public String getMessage() {
		return "Association error for user " + getUsername() + ": ";
	}
}

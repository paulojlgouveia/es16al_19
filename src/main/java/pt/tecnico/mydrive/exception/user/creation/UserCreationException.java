package pt.tecnico.mydrive.exception.user.creation;

import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class UserCreationException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String username;
	
	
	public UserCreationException(String username) {
		this.username = username;
	}
	
	
	public String getUsername() { return username; }
	
	
	@Override
	public String getMessage() {
		return "Error creating " + getUsername();
	}
}

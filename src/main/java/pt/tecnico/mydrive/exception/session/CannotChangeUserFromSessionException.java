package pt.tecnico.mydrive.exception.session;

import pt.tecnico.mydrive.exception.MyDriveException;

public class CannotChangeUserFromSessionException extends MyDriveException {
	
	private static final long serialVersionUID = 1L;
	private String username;
	
	public CannotChangeUserFromSessionException(String username) {
		this.username = username;
	}
	
	public String getUsername() { return username; }
	
	@Override
	public String getMessage() {
		return "Cannot change the user " + getUsername() + "from session.";
	}
}

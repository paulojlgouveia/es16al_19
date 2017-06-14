package pt.tecnico.mydrive.exception.user.access;

import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.domain.User;


public abstract class UserAccessException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final User user;
	
	public UserAccessException(User user) {
		this.user = user;
	}
	
	public User getUser() { return user; }

	public String getUsername() { return user.getUsername(); }

	@Override
	public String getMessage() {
		return "Error accessing user  " + getUsername();
	}
}

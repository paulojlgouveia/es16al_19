package pt.tecnico.mydrive.exception.user.access;

import pt.tecnico.mydrive.domain.User;


public class CannotChangeUserPasswordException extends UserAccessException {

	private static final long serialVersionUID = 1L;
	
	public CannotChangeUserPasswordException(User user) {
		super(user);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ", nobody user password's cannot be changed.";
	}
	
}

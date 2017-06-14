package pt.tecnico.mydrive.exception.user.access;

import pt.tecnico.mydrive.domain.User;


public class GuestUserRemovalException extends UserAccessException {

	private static final long serialVersionUID = 1L;
	
	public GuestUserRemovalException(User user) {
		super(user);
	}
	
	
	@Override
	public String getMessage() {
		return super.getMessage() + ", nobody user cannot be removed.";
	}

	
}



package pt.tecnico.mydrive.exception.user.access;

import pt.tecnico.mydrive.domain.User;


public class RootRemovalException extends UserAccessException {

	private static final long serialVersionUID = 1L;
	
	public RootRemovalException(User user) {
		super(user);
	}
	
	
	@Override
	public String getMessage() {
		return super.getMessage() + ", root user cannot be removed.";
	}

	
}



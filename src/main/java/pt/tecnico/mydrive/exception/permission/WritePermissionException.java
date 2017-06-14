package pt.tecnico.mydrive.exception.permission;

import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.File;


public class WritePermissionException extends PermissionException {

	private static final long serialVersionUID = 1L;

	public WritePermissionException(String user, String file) {
		super(user, file);
	}
	
	@Override
	public String getMessage() {
		return getUser() + " does not have write permissions on " + getFile();
	}

}

package pt.tecnico.mydrive.exception.permission;

import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.User;


public class ExecutePermissionException extends PermissionException {

	private static final long serialVersionUID = 1L;
	
	public ExecutePermissionException(String user, String file) {
		super(user, file);
	}

	@Override
	public String getMessage() {
		return getUser() + " does not have execute permissions on " + getFile();
	}
}

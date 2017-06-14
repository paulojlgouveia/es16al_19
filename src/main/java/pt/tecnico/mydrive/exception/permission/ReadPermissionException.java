package pt.tecnico.mydrive.exception.permission;

import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.User;


public class ReadPermissionException extends PermissionException {

	private static final long serialVersionUID = 1L;

	
	public ReadPermissionException(String user, String file) {
		super(user, file);
	}

	@Override
	public String getMessage() {
		return getUser() + " does not have read permissions on " + getFile();
	}
}

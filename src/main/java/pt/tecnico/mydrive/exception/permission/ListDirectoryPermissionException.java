package pt.tecnico.mydrive.exception.permission;

import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.User;


public class ListDirectoryPermissionException extends PermissionException {

	private static final long serialVersionUID = 1L;

	
	public ListDirectoryPermissionException(String user, String file) {
		super(user, file);
	}

	@Override
	public String getMessage() {
		return getUser() + " does not have list directory permissions on " + getFile();
	}
}

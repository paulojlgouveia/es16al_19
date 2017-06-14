package pt.tecnico.mydrive.exception.permission;

import pt.tecnico.mydrive.exception.MyDriveException;

import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.User;


public abstract class PermissionException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String user;
	private String file;
	
	
	public PermissionException(String user, String file) {
		this.user = user;
		this.file = file;
	}
		
	public String getUser() { return this.user; }
	
	public String getFile() { return this.file; }

	@Override
	public String getMessage() {
		return "Premission denied for " + getUser() + " on " + getFile();

	}
}

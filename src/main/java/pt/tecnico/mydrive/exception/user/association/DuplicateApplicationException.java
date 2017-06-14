package pt.tecnico.mydrive.exception.user.association;

import pt.tecnico.mydrive.exception.MyDriveException;

public class DuplicateApplicationException extends UserAssociationException {

	private static final long serialVersionUID = 1L;
    private final String appName;

	public DuplicateApplicationException(String username,String appName) {
		super(username);
		this.appName = appName;
	}

	@Override
	public String getMessage() {
		return "The application " + appName + " is already associated to an extension.\n";
	}
}

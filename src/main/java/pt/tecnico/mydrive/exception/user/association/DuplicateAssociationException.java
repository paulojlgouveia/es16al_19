package pt.tecnico.mydrive.exception.user.association;

import pt.tecnico.mydrive.exception.MyDriveException;

public class DuplicateAssociationException extends UserAssociationException {

	private static final long serialVersionUID = 1L;
    private final String extension;
    private final String appName;


	public DuplicateAssociationException(String username,String extension, String appName) {
		super(username);
        this.extension = extension;
        this.appName = appName;
	}



	@Override
	public String getMessage() {
		return "The extension " + extension + " is already associated to " + appName + ".\n";
	}
}

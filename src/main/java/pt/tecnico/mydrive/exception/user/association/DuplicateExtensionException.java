package pt.tecnico.mydrive.exception.user.association;

import pt.tecnico.mydrive.exception.MyDriveException;

public class DuplicateExtensionException extends UserAssociationException {

	private static final long serialVersionUID = 1L;
    private final String extension;



	public DuplicateExtensionException(String username,String extension) {
		super(username);
		this.extension = extension;
	}



	@Override
	public String getMessage() {
		return "The extension " + extension + " already exists.\n";
	}
}

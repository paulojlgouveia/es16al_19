package pt.tecnico.mydrive.exception.file.creation;


public class EmptyLinkException extends FileCreationException {

	private static final long serialVersionUID = 1L;
	
	
	public EmptyLinkException(String  filename) {
		super(filename);
	}
	
	@Override
	public String getMessage() {
		return "Failed to create: " + getFileName() + "\nLinks cannot be created without content.";
	}
}

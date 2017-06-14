package pt.tecnico.mydrive.exception.file.creation;

public class InvalidPathException extends FileCreationException {

	private static final long serialVersionUID = 1L;

	public InvalidPathException(String filename) {
		super(filename);
	}
	
	@Override
	public String getMessage() {
		return "Invalid path: " + getFileName() + "\n";
	}
}

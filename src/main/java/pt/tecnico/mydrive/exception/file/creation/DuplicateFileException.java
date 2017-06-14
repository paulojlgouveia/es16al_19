package pt.tecnico.mydrive.exception.file.creation;

public class DuplicateFileException extends FileCreationException {

	private static final long serialVersionUID = 1L;

	public DuplicateFileException(String filename) {
		super(filename);
	}

	@Override
	public String getMessage() {
		return "The file " + getFileName() + " already exists.\n";
	}
}

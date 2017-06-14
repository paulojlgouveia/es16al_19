package pt.tecnico.mydrive.exception.file.creation;

public class InvalidFileNameException extends FileCreationException {

	private static final long serialVersionUID = 1L;

	public InvalidFileNameException(String filename) {
		super(filename);
	}
	
	@Override
	public String getMessage() {
		if(getFileName().equals(""))
			return "Invalid empty file name.\n";
		else
			return "Invalid file name: " + getFileName() + "\nContains an illegal character.\n";
	}
}

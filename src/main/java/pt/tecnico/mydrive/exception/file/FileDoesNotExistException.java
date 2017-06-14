package pt.tecnico.mydrive.exception.file;

import pt.tecnico.mydrive.exception.MyDriveException;


public class FileDoesNotExistException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String filename;

	
	public FileDoesNotExistException(String filename) {
		this.filename = filename;
	}

	
	public String getFileName() { return filename; }

	@Override
	public String getMessage() {
		return "The File " + getFileName() + " does not exist.";
	}
}

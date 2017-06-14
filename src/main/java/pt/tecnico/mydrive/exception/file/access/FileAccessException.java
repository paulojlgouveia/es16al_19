package pt.tecnico.mydrive.exception.file.access;

import pt.tecnico.mydrive.exception.MyDriveException;
import pt.tecnico.mydrive.domain.File;


public abstract class FileAccessException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String file;
	
	public FileAccessException(String  file) {
		this.file = file;
	}
	
	public String getFilename() { return file; }

	@Override
	public String getMessage() {
		return "Error accessing file  " + getFilename();
	}
}

package pt.tecnico.mydrive.exception.file.execution;

import pt.tecnico.mydrive.exception.MyDriveException;


public class FileExecutionException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String filename;

	
	public FileExecutionException(String filename) {
		this.filename = filename;
	}

	
	public String getFileName() { return filename; }

	@Override
	public String getMessage() {
		return "Error executing file  " + getFileName();
	}
}

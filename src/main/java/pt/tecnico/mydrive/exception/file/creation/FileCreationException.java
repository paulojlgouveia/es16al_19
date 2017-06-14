package pt.tecnico.mydrive.exception.file.creation;

import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class FileCreationException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String  filename;
	
	public FileCreationException(String  filename) {
		this.filename = filename;
	}
		
	public String getFileName() { return filename; }

	@Override
	public String getMessage() {
		return "Error creating " + getFileName();
	}
}

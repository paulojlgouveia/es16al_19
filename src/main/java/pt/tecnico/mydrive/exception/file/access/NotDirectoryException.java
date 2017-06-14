package pt.tecnico.mydrive.exception.file.access;

import pt.tecnico.mydrive.domain.File;


public class NotDirectoryException extends FileAccessException {

	private static final long serialVersionUID = 1L;

	public NotDirectoryException(String  file) {
		super(file);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": it is not a directory and cannot be listed.";
	}
}
package pt.tecnico.mydrive.exception.file.access;

import pt.tecnico.mydrive.domain.File;


public class NotEditableException extends FileAccessException {

	private static final long serialVersionUID = 1L;
	
	public NotEditableException(String  file) {
		super(file);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": it does not have editable content.";
	}
	
}

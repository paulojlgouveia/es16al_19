package pt.tecnico.mydrive.exception.file.access;

import pt.tecnico.mydrive.domain.File;


public class NotReadableException extends FileAccessException {

	private static final long serialVersionUID = 1L;
	
	public NotReadableException(String  file) {
		super(file);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": it does not have readable content.";
	}
	
}



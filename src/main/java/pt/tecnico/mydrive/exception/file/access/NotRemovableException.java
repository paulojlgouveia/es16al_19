package pt.tecnico.mydrive.exception.file.access;

import pt.tecnico.mydrive.domain.File;


public class NotRemovableException extends FileAccessException {

	private static final long serialVersionUID = 1L;
	
	
	public NotRemovableException(String  file) {
		super(file);
	}
	
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": it cannot be removed.";
	}

}



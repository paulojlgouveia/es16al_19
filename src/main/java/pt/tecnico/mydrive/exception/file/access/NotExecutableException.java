package pt.tecnico.mydrive.exception.file.access;

import pt.tecnico.mydrive.domain.File;


public class NotExecutableException extends FileAccessException {

	private static final long serialVersionUID = 1L;
	
	
	public NotExecutableException(String file) {
		super(file);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": it is not an executable file.";
	}
	
	
	
	
// 	public NotExecutableException(File  file) {
// 		super(file);
// 	}
// 	
// 	@Override
// 	public String getMessage() {
// 		return super.getMessage() + ": it is not an executable file.";
// 	}
	
}

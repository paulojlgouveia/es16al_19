package pt.tecnico.mydrive.exception.file.execution;

import pt.tecnico.mydrive.domain.File;


public class InvalidClassException extends FileExecutionException {
	private String className;
	private static final long serialVersionUID = 1L;

	public InvalidClassException(File  file,String className) {
		super(file.getName());
		this.className = className;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": " + className + " is not a valid class name.";
	}
}
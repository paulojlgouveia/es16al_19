package pt.tecnico.mydrive.exception.file.execution;

import pt.tecnico.mydrive.domain.File;


public class InvalidMethodException extends FileExecutionException {
	private String methodName;
	private static final long serialVersionUID = 1L;

	public InvalidMethodException(File  file,String methodName) {
		super(file.getName());
		this.methodName = methodName;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + ": " + methodName + " is not a valid method name.";
	}
}
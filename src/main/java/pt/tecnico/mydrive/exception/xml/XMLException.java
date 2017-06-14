package pt.tecnico.mydrive.exception.xml;

import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class XMLException extends MyDriveException {
    
	private static final long serialVersionUID = 1L;
	private final String className;
	private final String objectName;
	private final String insideError;

// 	public XMLException() {}

	public XMLException(String problemClass, String objectName) {
		this.className = problemClass;
		this.objectName = objectName;
		this.insideError = "";
	}

	public XMLException(String problemClass, String objectName, String error) {
		this.className = problemClass;
		this.objectName = objectName;
		this.insideError = error;
	}

	public String getClassName() { return className; }

	public String getObjectName() { return objectName; }

	public String getInsideError() { return insideError; }
	
	@Override
	public String getMessage() {
		return "XML error from " + getObjectName() + "Object.";
	}
    
}
package pt.tecnico.mydrive.exception.file;

import pt.tecnico.mydrive.exception.MyDriveException;

public class InvalidLinkException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private final String content;
	
	public InvalidLinkException(String content) {
		this.content = content;
	}
	
	@Override
	public String getMessage() {
		return "Invalid Link. There is no file with the path: " + content;
	}
}
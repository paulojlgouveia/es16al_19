package pt.tecnico.mydrive.exception.session;

import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class SessionException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private long token;
	
	public SessionException(long token) {
		this.token = token;
	}
	
	public long getToken() { return token; }
	
	@Override
	public String getMessage() {
		return "There was an error with session by the token " + getToken();
	}
}

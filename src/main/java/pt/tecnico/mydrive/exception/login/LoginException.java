package pt.tecnico.mydrive.exception.login;

import pt.tecnico.mydrive.exception.MyDriveException;


public abstract class LoginException extends MyDriveException {

	private static final long serialVersionUID = 1L;
	private String _username;
	private long _token;

	public LoginException(String username) {
		_username = username;
	}
	
	public LoginException(long token) {
		_token = token;
	}
	
	public String getUsername() {
		return _username;
	}
	
	public long getToken() {
		return _token;
	}
	
	@Override
	public abstract String getMessage();
}

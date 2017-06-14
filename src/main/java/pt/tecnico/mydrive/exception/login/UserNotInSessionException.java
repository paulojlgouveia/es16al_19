package pt.tecnico.mydrive.exception.login;

public class UserNotInSessionException extends LoginException {
	
	private static final long serialVersionUID = 1L;

	public UserNotInSessionException(long token) {
		super(token);
	}
	
	@Override
	public String getMessage() {
		return "The User identified by the token " + getToken() + " is not in session\n";
	}	
}

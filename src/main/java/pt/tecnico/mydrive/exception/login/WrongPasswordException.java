package pt.tecnico.mydrive.exception.login;

public class WrongPasswordException extends LoginException {

	private static final long serialVersionUID = 1L;
	
	public WrongPasswordException(String username) {
		super(username);
	}

	@Override
	public String getMessage() {
		return "The password of the user " + getUsername() + " is incorrect.\n";
	}
}

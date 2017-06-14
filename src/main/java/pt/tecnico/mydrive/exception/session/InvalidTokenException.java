package pt.tecnico.mydrive.exception.session;


public class InvalidTokenException extends SessionException {

	private static final long serialVersionUID = 1L;
	
	public InvalidTokenException(long token) {
		super(token);
	}
	
	@Override
	public String getMessage() {
		return "There is no session with the token " + getToken();
	}
}

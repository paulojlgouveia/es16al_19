package pt.tecnico.mydrive.exception.session;

public class CannotChangeSessionException extends SessionException {

	private static final long serialVersionUID = 1L;
	
	public CannotChangeSessionException(long token) {
		super(token);
	}
	
	@Override
	public String getMessage() {
		return "Cannot change Session with the token " + getToken();
	}
}

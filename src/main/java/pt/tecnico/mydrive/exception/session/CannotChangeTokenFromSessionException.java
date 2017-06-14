package pt.tecnico.mydrive.exception.session;

public class CannotChangeTokenFromSessionException extends SessionException {

	private static final long serialVersionUID = 1L;
	
	public CannotChangeTokenFromSessionException(long token) {
		super(token);
	}
	
	@Override
	public String getMessage() {
		return "Cannot change the token "  + getToken() + " from Session.";
	}
}
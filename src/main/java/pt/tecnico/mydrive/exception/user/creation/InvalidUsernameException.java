package pt.tecnico.mydrive.exception.user.creation;

public class InvalidUsernameException extends UserCreationException {

	private static final long serialVersionUID = 1L;
	private final String reason;


	public InvalidUsernameException(String username) {
		super(username);
		this.reason = null;

	}
	
	public InvalidUsernameException(String username, String reason) {
		super(username);
		this.reason = reason;
	}
	
	
	public String getReason() { return reason; }

	
	@Override
	public String getMessage() {
		if (getReason() == null)
			return super.getMessage();
		else	
			return "The username " + getUsername() + " is invalid because " + getReason() + "\n";
	}
}

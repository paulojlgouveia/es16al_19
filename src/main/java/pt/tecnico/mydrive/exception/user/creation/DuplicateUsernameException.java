package pt.tecnico.mydrive.exception.user.creation;

public class DuplicateUsernameException extends UserCreationException {

	private static final long serialVersionUID = 1L;

	public DuplicateUsernameException(String username) {
		super(username);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " username is already in use.\n";
	}
}

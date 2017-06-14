package pt.tecnico.mydrive.exception.user;

import pt.tecnico.mydrive.exception.MyDriveException;

public class PasswordLengthException extends MyDriveException {

	private static final long serialVersionUID = 1L;
    private final String password;
    private final String reason;


	public PasswordLengthException(String password, String reason) {
        this.password = password;
        this.reason = reason;
	}
	
	public String getPassword() { return password; }
    
    public String getReason() { return reason; }

	@Override
	public String getMessage() {
		return "The password " + getPassword() + " is invalid because " + getReason() + "\n";
	}
}

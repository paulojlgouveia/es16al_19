package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.exception.user.UserDoesNotExistException;

public class LoginUserService extends MyDriveService {

	private String username;
	private String password;
	private long token;
	private boolean isKey;

	public LoginUserService(String username, String password) {
		this.username = username;
		this.password = password;
		this.isKey = false;
	}

	public LoginUserService(String username) {
		this(username, "");
	}

	public LoginUserService(String username, boolean key) {
		this(username);
		this.isKey = key;
	}

	@Override
	public final void dispatch() {
		MyDriveManager mydrive = getMyDriveManager();

		if (isKey == true) {
			
			if (mydrive.getUserByUsername(username) == null)
				throw new UserDoesNotExistException(username);

			token = mydrive.getTokenByUsername(username);

		} else if (username.equals("nobody") && ((password.equals("") || password == null))) {
			token = new Session(mydrive, username).getToken();
		} else
			token = new Session(mydrive, username, password).getToken();
	}

	public long result() {
		return token;
	}

}

package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LoginUserService;

public class Key extends MdCommand {

	public Key(Shell sh) {
		super(sh, "token", "token of the current user");
	}

	public void execute(String[] args) {
		String username;
		long token;

		if (args.length > 1)
			throw new RuntimeException("USAGE: " + name() + " [<username>]");
		if (args.length == 0) {
			token = ((MyDrive) shell()).getToken();
			username = ((MyDrive) shell()).getUsername();
			shell().println("Username: " + username + "\nToken: " + token);
		}
		else{
			LoginUserService loginUser = new LoginUserService(args[0], true);
			loginUser.execute();
			token = loginUser.result();
			((MyDrive) shell()).setUsername(args[0]);
			username = ((MyDrive) shell()).getUsername();
			((MyDrive) shell()).setToken(token);
			shell().println("User changed.\nNew Username: " + username + "\nNew Token: " + token);
		}
	}
}
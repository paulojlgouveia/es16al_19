package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LoginUserService;

public class Login extends MdCommand {

	public Login(Shell sh) {
		super(sh, "login", "login of the user");
	}

	public void execute(String[] args) {
		long token;

		if (args.length < 1 || args.length > 2)
			throw new RuntimeException("USAGE: " + name() + " <username> [<password>]");
		if (args.length == 1) {
			LoginUserService loginNobody = new LoginUserService(args[0]);
			loginNobody.execute();
			token = loginNobody.result();
			((MyDrive) shell()).setUsername(args[0]);
			((MyDrive) shell()).setToken(token);
		} else {
			LoginUserService loginUser = new LoginUserService(args[0], args[1]);
			loginUser.execute();
			token = loginUser.result();
			((MyDrive) shell()).setUsername(args[0]);
			((MyDrive) shell()).setToken(token);
		}
	}

}
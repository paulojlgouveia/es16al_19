package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ChangeDirectoryService;

public class ChangeDirectory extends MdCommand {

	public ChangeDirectory(Shell sh) {
		super(sh, "cwd", "Change working directory");
	}
	
	public void execute(String[] args) {
		ChangeDirectoryService cwdService;
		long token;
		
		if (args.length <= 1) {
			token = ((MyDrive) shell()).getToken();
			
			if (args.length == 1)
				cwdService = new ChangeDirectoryService(token, args[0]);
			else
				cwdService = new ChangeDirectoryService(token, ".");
			
			cwdService.execute();
			shell().println("CurrentDir: " + cwdService.result());
		} else {
			throw new RuntimeException("USAGE: " + name() + " [path]");
		}
	}
}
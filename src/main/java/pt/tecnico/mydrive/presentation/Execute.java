package pt.tecnico.mydrive.presentation;

import java.util.Arrays;

import pt.tecnico.mydrive.service.ExecuteFileService;

public class Execute extends MdCommand {

	public Execute(Shell sh) {
		super(sh, "do", "Execute application");
	}
	
	public void execute(String[] args) {
		long token;
		
		if(args.length == 1) {
			token = ((MyDrive) shell()).getToken();
			
			ExecuteFileService execService = new ExecuteFileService(token, args[0], new String[0]);
			execService.execute();
			
		// file path and arguments
		} else if(args.length > 1) {
			token = ((MyDrive) shell()).getToken();
			String[] executeArgs = Arrays.copyOfRange(args, 1, args.length);
			ExecuteFileService execService = new ExecuteFileService(token, args[0], executeArgs);
			execService.execute();
		// wrong number of arguments
		} else
			throw new RuntimeException("USAGE: " + name() + " <file-path> <arguments>");
	}
}


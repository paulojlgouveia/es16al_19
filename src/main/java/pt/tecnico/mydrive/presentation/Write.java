package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.WriteFileService;

public class Write extends MdCommand {
	public Write(Shell sh) {
		super(sh, "write", "change content of file to text");
	}

	public void execute(String[] args){
		if (args.length != 2)
			throw new RuntimeException("USAGE: " + name() + " <path> <text>");

		long token = ((MyDrive) shell()).getToken();
		String path = args[0];
		String name;

		String[] folders = path.split("/");
		if(folders.length == 0)
			name = "";
		else
			name = folders[folders.length -1];

		WriteFileService wfs = new WriteFileService(token, name, args[1]);
		wfs.execute();
	}
}
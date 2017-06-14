package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.ListDirectoryService;
import pt.tecnico.mydrive.service.ChangeDirectoryService;
import pt.tecnico.mydrive.service.FileType;
import pt.tecnico.mydrive.service.dto.FileDto;

public class List extends MdCommand {
	
	public List(Shell sh){
		super(sh, "ls", "list the current directory's entries.");
	}
	
	public void execute(String[] args){
		long token = ((MyDrive) shell()).getToken();
		ListDirectoryService lds = new ListDirectoryService(token);
		
		if(args.length == 0){
			lds.execute();
			for(FileDto dto : lds.result()){
				if(dto.getFileType().equals("Link")){
					shell().println(dto.getFileType() + " " + dto.getPermissions() + " " + dto.getDimension() + " " + 
			                dto.getOwnerName() + " " + dto.getId() + " " + dto.getLastModified() + " " + 
					        dto.getName() + " -> " + dto.getContent());
				}
				else{
					shell().println(dto.getFileType() + " " + dto.getPermissions() + " " + dto.getDimension() + " " + 
					                dto.getOwnerName() + " " + dto.getId() + " " + dto.getLastModified() + " " + 
							        dto.getName());
				}
			}
		}
		else if(args.length == 1){
			
			ChangeDirectoryService cds1 = new ChangeDirectoryService(token,args[0]);
			ChangeDirectoryService cds2 = new ChangeDirectoryService(token,".");
			cds2.execute();
			ChangeDirectoryService cds3 = new ChangeDirectoryService(token,cds2.result());
			
			
			cds1.execute();
			lds.execute();
			for(FileDto dto : lds.result()){
				shell().println(dto.getFileType() + " " + dto.getPermissions() + " " + dto.getDimension() + " " + 
				                dto.getOwnerName() + " " + dto.getId() + " " + dto.getLastModified() + " " + 
						        dto.getName());
			}
			cds3.execute();
		} 
		else {
			throw new RuntimeException("USAGE: " + name() + " <path>");
		}
	}
}

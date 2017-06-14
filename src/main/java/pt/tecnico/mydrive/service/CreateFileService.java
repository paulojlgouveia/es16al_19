package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Application;
import pt.tecnico.mydrive.domain.Link;

import pt.tecnico.mydrive.exception.permission.WritePermissionException;
import pt.tecnico.mydrive.exception.file.creation.UnreconizedFileTypeException;
import pt.tecnico.mydrive.exception.file.creation.EmptyLinkException;
import pt.tecnico.mydrive.exception.file.creation.InvalidFileNameException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;


public class CreateFileService extends MyDriveService {

	private long token;
	private FileType fileType;
	private String name;
	private String content;

	
	public CreateFileService(long token, FileType fileType, String name) {
		this.token = token;
		this.fileType = fileType;
		this.name = name;
		this.content = "";
	}


	public CreateFileService(long token, FileType fileType, String name, String content) {
		this.token = token;
		this.fileType = fileType;
		this.name = name;
		this.content = content;
	}

	
	private void verifyFileName(String fileName) {
		if(fileName.equals(""))
			throw new InvalidFileNameException(fileName);
		for(char c : fileName.toCharArray())
			if(c == '\0' || c == '/')
				throw new InvalidFileNameException(fileName);
	}

	@Override
	public final void dispatch() {
		MyDriveManager mydrive = MyDriveManager.getInstance();
		Session session = mydrive.getSessionByToken(token);
		
		if(session == null)
			throw new InvalidTokenException(token);
		
		if(session.isValid()) {
			session.renew();
			User user = session.getUser();
			
			verifyFileName(this.name);
			if(!user.hasWritePermission(session.getCurrentDirectory())){
				throw new WritePermissionException(user.getUsername(),
												   session.getCurrentDirectory().getAbsolutePath());
			}
			if(fileType == FileType.DIRECTORY) {
				// ignores content if provided
				new Directory(mydrive, user, session.getCurrentDirectory(), name);
				
			} else if(fileType == FileType.PLAINFILE) {
				new PlainFile(mydrive, user, session.getCurrentDirectory(), name, content);
				
			} else if(fileType == FileType.APPLICATION) {
				new Application(mydrive, user, session.getCurrentDirectory(), name, content);
				
			} else if(fileType == FileType.LINK) {
				if(content.equals("") || content == null)
					throw new EmptyLinkException(name);
					
				new Link(mydrive, user, session.getCurrentDirectory(), name, content);
				
			} else
				throw new UnreconizedFileTypeException(name, ""+fileType);
			
		} else
			log.warn("Session " + token + " has expired.");
	}
	
	
// 	@Override
// 	public final void dispatch() {
// 		MyDriveManager mydrive = MyDriveManager.getInstance();
// 		Session session = mydrive.getSessionByToken(token);
//
// 		if(session == null)
// 			throw new InvalidTokenException(token);
//
// 		if(session.isValid()) {
// 			session.renew();
// 			User user = session.getUser();
//
// 			if(fileType.equals("Directory") || fileType.equals("directory")) {
// 				// ignores content if provided
// 				new Directory(mydrive, user, session.getCurrentDirectory(), name);
// 			} else if(fileType.equals("PlainFile") || fileType.equals("plainfile")) {
// 				new PlainFile(mydrive, user, session.getCurrentDirectory(), name, content);
//
// 			} else if(fileType.equals("Application") || fileType.equals("application")) {
// 				new Application(mydrive, user, session.getCurrentDirectory(), name, content);
//
// 			} else if(fileType.equals("Link") || fileType.equals("link")) {
// 				if(content.equals("") || content == null)
// 					throw new EmptyLinkException(name);
// 				new Link(mydrive, user, session.getCurrentDirectory(), name, content);
//
// 			} else
// 				throw new UnreconizedFileTypeException(name, fileType);
//
// 		} else
// 			log.warn("Session " + token + " has expired.");
// 	}
}





package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.File;

import pt.tecnico.mydrive.exception.session.InvalidTokenException;

import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.permission.ExecutePermissionException;


public class ExecuteFileService extends MyDriveService{
	
	private long token;
	private String path;
	private String[] args;
	private User user;
	private File file;
	private Session session;
	private MyDriveManager mydrive;
	
	public ExecuteFileService(long token,String path,String[] args){
		this.token = token;
		this.path = path;
		this.args = args;
	}
	
	@Override
	public void dispatch(){
		mydrive = MyDriveManager.getInstance();
		session = mydrive.getSessionByToken(token);

        
        if(session == null){
        	throw new InvalidTokenException(token);
        }
        
        if(session.isValid()){
        	session.renew();
        	user = session.getUser();

        	file = mydrive.getFileByPath(session.getCurrentDirectory(),path);
        	
        	if(file == null){
				throw new FileDoesNotExistException(path);
        	}
        	else if(!user.hasExecutePermission(file)){
        		throw new ExecutePermissionException(user.getUsername(),file.getName());
        	}
        	else{
        		file.execute(mydrive, args);
        	}
        	
        }
        else{
        	log.warn("Session " + token + " has expired.");
        }
	}
}
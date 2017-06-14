package pt.tecnico.mydrive.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Link;

import pt.tecnico.mydrive.service.dto.FileDto;

import pt.tecnico.mydrive.exception.session.InvalidTokenException;
import pt.tecnico.mydrive.exception.permission.ReadPermissionException;

public class ListDirectoryService extends MyDriveService {

	private List<FileDto> fileListing;
	private long token;

    public ListDirectoryService(long token) {
    	this.token = token;
    }

    @Override
    public final void dispatch() {
    	MyDriveManager mydrive = getMyDriveManager();
    	Session session = mydrive.getSessionByToken(token);
    	fileListing = new ArrayList<FileDto>();
    	String aux,filename;
    	Directory currdir;
    	User user;
    	FileDto dto;
    	
    	if(session == null){
    		throw new InvalidTokenException(token);
    	}    	
    	if(session.isValid()){
    		session.renew();
    		currdir = session.getCurrentDirectory();
    		user = mydrive.getUserByToken(token);
    		if(user.hasReadPermission(currdir)){
    			for(File f : currdir.getFileSet()){
    				dto = new FileDto(f.getClass().getSimpleName(),f.getPermissions(),
        					f.getSize(),f.getOwner().getUsername(), f.getId(),f.getName(),
        					f.getLastModifiedDate());
    				if(f.getClass().getSimpleName().equals("Link")){
    					dto.setContent(((Link) f).getContent());
    				}
    				fileListing.add(dto);
    				
        			
        			
        		}
        		fileListing.add(new FileDto("Directory",currdir.getPermissions(),
    					currdir.getSize(),currdir.getOwner().getUsername(), currdir.getId(),".",
    					currdir.getLastModifiedDate()));
        		
        		fileListing.add(new FileDto("Directory",currdir.getParentDirectory().getPermissions(),
    					currdir.getParentDirectory().getSize(),currdir.getParentDirectory().getOwner().getUsername(),
    					currdir.getParentDirectory().getId(),"..",
    					currdir.getParentDirectory().getLastModifiedDate()));
        		
        		Collections.sort(fileListing);
    		}
    		else{
    			throw new ReadPermissionException(user.getUsername(),currdir.getName());
    		}	
    	}
    	else{
    		log.warn("Session " + token + " has expired.");
    	}
    }
    
    public final List<FileDto> result(){
    	return fileListing;
    }
}

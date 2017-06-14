package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Application;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.exception.permission.DeletePermissionException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

import pt.tecnico.mydrive.exception.permission.DeletePermissionException;
import pt.tecnico.mydrive.exception.permission.WritePermissionException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;

import java.util.Set;


public class DeleteFileService extends MyDriveService {

    private int id;
    private String name;
    private String permissions;
    private User owner;
    private File file;
    private long token;
    private Session session;
    private MyDriveManager mydrive;

    public DeleteFileService(long token, String name) {
        this.token = token;
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    public long getToken(){
        return token;
    }

    @Override
    public final void dispatch() {
        mydrive = MyDriveManager.getInstance();
        session = mydrive.getSessionByToken(token);

        if(session == null)
            throw new InvalidTokenException(token);

        if(session.isValid()){
            session.renew();
            file = session.getCurrentDirectory().getEntry(name);
            if(file == null)
                throw new FileDoesNotExistException(name);

            owner = session.getUser();
            if(owner.hasDeletePermission(file)){
            	if(!owner.hasWritePermission(session.getCurrentDirectory())){
            		throw new WritePermissionException(owner.getUsername(), 
            											session.getCurrentDirectory().getAbsolutePath());
            	}
                file.remove();
            }
            else
                throw new DeletePermissionException(owner.getUsername(), file.getAbsolutePath());
        }
        else
            log.warn("Session " + token + " has expired.");
    }
}

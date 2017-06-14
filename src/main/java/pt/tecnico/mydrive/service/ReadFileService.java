package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.file.access.NotReadableException;
import pt.tecnico.mydrive.exception.permission.ReadPermissionException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

public class ReadFileService extends MyDriveService {
	private final long _token;
	private final String _name;
	private String _content;
	
    public ReadFileService(long token, String name) {
    	_token = token;
    	_name = name;
    }

    public long getToken(){
    	return _token;
    }

    public String getName(){
    	return _name;
    }

    @Override
    public final void dispatch() throws NotReadableException, ReadPermissionException, FileDoesNotExistException, InvalidTokenException{
    	MyDriveManager md = getMyDriveManager();
    	Session s = md.getSessionByToken(_token);
    	User u;
    	File f;

    	// Session Invalid
    	if(s == null)
    		throw new InvalidTokenException(_token);

        // Session Valid
        if(s.isValid()){
            s.renew();

            // File doesnt exist
            f = s.getCurrentDirectory().getEntry(_name);
            if(f == null)
                throw new FileDoesNotExistException(_name);

            // User doesn't have permission to access Plaintext
            u = md.getUserByToken(_token);
            if(!u.hasReadPermission(f))
                throw new ReadPermissionException(u.getUsername(), f.getAbsolutePath());
			
			_content = f.readContent();
        } else {
         log.warn("Session " + _token + " has expired.");
       }
    	
    }

    public final String result(){
    	return _content;
    }
}

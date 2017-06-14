package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.file.access.NotEditableException;
import pt.tecnico.mydrive.exception.permission.WritePermissionException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

public class WriteFileService extends MyDriveService {
	private final long _token;
	private final String _name;
	private String _content;

	public WriteFileService(long token, String name, String content) {
		_token = token;
		_name = name;
		_content = content;
	}

	public long getToken(){
		return _token;
	}

	public String getName(){
		return _name;
	}

	public String getContent(){
		return _content;
	}
	
	@Override
	public final void dispatch() throws InvalidTokenException, FileDoesNotExistException, NotEditableException, WritePermissionException {

		MyDriveManager md = getMyDriveManager();
		Session s = md.getSessionByToken(_token);
		User u;
		File f;
		
		// Session Invalid
		if(s == null)
			throw new InvalidTokenException(_token);
		
		// Session Valid
		if(s.isValid()) {
			s.renew();
			// File doesnt exist
			f = s.getCurrentDirectory().getEntry(_name);
			if(f == null)
				throw new FileDoesNotExistException(_name);
			
			// User doesn't have permission to access Plaintext
			u = md.getUserByToken(_token);
			if(!u.hasWritePermission(f))
				throw new WritePermissionException(u.getUsername(), f.getAbsolutePath());

			f.writeContent(_content);
		} else {
			log.warn("Session " + _token + " has expired.");
		}
	}
}

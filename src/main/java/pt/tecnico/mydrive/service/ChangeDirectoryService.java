package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

public class ChangeDirectoryService extends MyDriveService {

	private long token;
	private String path;
	private String returnPath;

    public ChangeDirectoryService(long token, String path) {
		this.token = token;
		this.path = path;
    }

    @Override
    public final void dispatch() {
    	MyDriveManager mydrive = getMyDriveManager();
    	Session session = mydrive.getSessionByToken(token);
    	
    	if(session == null)
			throw new InvalidTokenException(token);
			
		if(session.isValid()) {
			session.renew();
			returnPath = session.changeDirectory(mydrive, path);
		} else {
			log.warn("Session " + token + " has expired.");
		}
    }
    
    public final String result() {
    	return returnPath;
    }
}

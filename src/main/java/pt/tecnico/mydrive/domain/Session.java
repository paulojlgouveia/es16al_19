package pt.tecnico.mydrive.domain;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;
import java.math.BigInteger;

import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.file.access.NotDirectoryException;
import pt.tecnico.mydrive.exception.permission.ExecutePermissionException;
import pt.tecnico.mydrive.exception.session.CannotChangeTokenFromSessionException;
import pt.tecnico.mydrive.exception.session.CannotChangeUserFromSessionException;
import pt.tecnico.mydrive.exception.user.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.login.WrongPasswordException;

public class Session extends Session_Base {

//Session_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/
//		long token;
//		DateTime dateOfLastAccess;

// constructors //
	
	public Session() {}
	
	public Session(MyDriveManager mydrive, String username) throws UserDoesNotExistException {
		User user = mydrive.getUserByUsername(username);
		
		if(user == null)
			throw new UserDoesNotExistException(username);
		
		init(mydrive, user);
	}
	
	public Session(MyDriveManager mydrive, String username, String password)
			throws UserDoesNotExistException, WrongPasswordException {

		User user = mydrive.getUserByUsername(username);
		
		if(user == null)
			throw new UserDoesNotExistException(username);
		if(!user.getPassword().equals(password))
    		throw new WrongPasswordException(username);
		
		mydrive.removeInvalidSessions();

		init(mydrive, user);
	}
	
// Init //
	
	protected void init(MyDriveManager mydrive, User user) {
		DateTime date =  new DateTime();
		setDateOfLastAccess(date);
		
		super.setToken(generateToken(mydrive));
		super.setUser(user);
		
		setCurrentDirectory(user.getHomeDirectory());
		
		mydrive.addSession(this);
		
//		mydrive.listSessions(); // FIXME REMOVE THIS INSTRUCTION BEFORE SUBMISSION
	}
	
	
// functions //
	
	@Override
	public void setToken(long token) {
		throw new CannotChangeTokenFromSessionException(token);
	}
	
	@Override
	public void setUser(User user) {
		throw new CannotChangeUserFromSessionException(user.getUsername());
	}
	
	public long generateToken(MyDriveManager mydrive) {
        Random rand = new Random();
        long token = new BigInteger(64, rand).longValue();
        
        // ensure there are not repeated tokens
		while(mydrive.getSessionByToken(token) != null)
			token = new BigInteger(64, rand).longValue();
			
        return token;
    }
	
	public void renew() {
		setDateOfLastAccess(new DateTime());
	}
	
	
	public boolean isValid() {
		return getUser().validSessionTime(this);
	}

	
	// FIXME REMOVE THIS FUNCTION BEFORE SUBMISSION
	public String toString() {
		String out = "";
			
		out += "Token: " + getToken() + "\n"
			+ "User: " + getUser().getUsername() + "\n"
			+ "CurrentDir: " + getCurrentDirectory().getAbsolutePath() + "\n"
			+ "DateOfLastAccess: " + getDateOfLastAccess() + "\n";
		
		return out;
	}
	
	public void remove() {
		setMyDriveManager(null);
		super.setUser(null);
		setCurrentDirectory(null);
		deleteDomainObject();
	}
	
		
	public void setVariable(String varName, String value){
		for (Variable var : getVariableSet()){
			if (var.getName().equals(varName))
				var.setValue(value);
				return;
		}
		
		addVariable(new Variable(this, varName, value));
	}
	
	public List<String> getVariableList(){
		List<String> varList = new ArrayList<String>();
		
		for (Variable var : getVariableSet())
			varList.add(var.getName() + "=" + var.getValue());
		
		return varList;
	}
	
// operations //

	public String changeDirectory(MyDriveManager mydrive, String path) throws FileDoesNotExistException,
				NotDirectoryException, ExecutePermissionException {
		
		File dir = mydrive.getFileByPath(getCurrentDirectory(), path);
		
		if(dir == null)
			throw new FileDoesNotExistException(path);
		
		dir = dir.getEntry("."); // throws NotDirectoryException if not a directory
		
		if(!getUser().hasExecutePermission(dir))
			throw new ExecutePermissionException(getUser().getUsername(), dir.getAbsolutePath());
		
		setCurrentDirectory((Directory) dir);
		return dir.getAbsolutePath();
	}
}


package pt.tecnico.mydrive.domain;
import pt.tecnico.mydrive.exception.user.access.CannotChangeUserPasswordException;
import pt.tecnico.mydrive.exception.user.access.GuestUserRemovalException;


public class GuestUser extends GuestUser_Base {
    
//Guest_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/
	
	
// constructors //
	
    public GuestUser(MyDriveManager mydrive) {
    	setUsername("nobody");
    	setName("Guest");
    	super.setPassword("");
    	setMask("rxwdr-x-");
    	    	
    	setMyDriveManager(mydrive);
    	
    	Directory home = new Directory(mydrive, this, mydrive.getHomeDirectory(), getUsername());
    	setHomeDirectory(home);
    }
    

// functions //
	
	@Override
	public boolean tokenNeverExpires() {
		return true;
	}
    
	@Override
	public void setPassword(String password) {
		throw new CannotChangeUserPasswordException(this);
	}
    
	@Override
	public boolean hasReadPermission(File file) {
		return true;
	}
	
	@Override
	public boolean hasWritePermission(File file) {
		return false;
	}
	
	@Override
	public boolean hasExecutePermission(File file) {
		return true;
	}
	
	@Override
	public boolean hasDeletePermission(File file) {
		return false;
	}

	@Override
	public boolean validSessionTime(Session s){
    	return true;
    }
    
    @Override
    public void remove() throws GuestUserRemovalException {
    	throw new GuestUserRemovalException(this);
    }
    
}

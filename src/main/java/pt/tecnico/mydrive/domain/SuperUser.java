package pt.tecnico.mydrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.user.access.RootRemovalException;


public class SuperUser extends SuperUser_Base {

//SuperUser_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/


// init functions //


// constructors //
    
    public SuperUser(MyDriveManager mydrive) {
    	setUsername("root");
    	setName("Super User");
    	setPassword("***");
    	setMask("rwxdr-x-");
    	
    	createSystemDirectories(mydrive);
    	
    	Directory home = new Directory(mydrive, this, mydrive.getHomeDirectory(), getUsername());
		setHomeDirectory(home);
    	
		setMyDriveManager(mydrive);
    }
	
	
// functions //
	
	public void createSystemDirectories(MyDriveManager mydrive) {
		new Directory(mydrive, this, "/");
		new Directory(mydrive, this, "home");
    }
	
	@Override
	public boolean hasReadPermission(File file) {
		return true;
	}
	
	@Override
	public boolean hasWritePermission(File file) {
		return true;
	}
	
	@Override
	public boolean hasExecutePermission(File file) {
		return true;
	}
	
	@Override
	public boolean hasDeletePermission(File file) {
		return true;
	}

	@Override
	public boolean validSessionTime(Session s){
		DateTime aux = new DateTime();
    	return (aux.getMillis() - s.getDateOfLastAccess().getMillis()) < 600000; // 10min = 600,000ms
    }
	
	@Override
    public void remove() throws RootRemovalException {
		throw new RootRemovalException(this);
    }

}

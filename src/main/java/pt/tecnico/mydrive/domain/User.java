package pt.tecnico.mydrive.domain;

import org.joda.time.DateTime;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.tecnico.mydrive.exception.xml.*;
import pt.tecnico.mydrive.exception.user.creation.InvalidUsernameException;
import pt.tecnico.mydrive.exception.user.PasswordLengthException;
//import pt.tecnico.mydrive.exception.user.creation.DuplicateUsernameException;

import pt.tecnico.mydrive.exception.user.association.DuplicateAssociationException;
import pt.tecnico.mydrive.exception.user.association.DuplicateExtensionException;
import pt.tecnico.mydrive.exception.user.association.DuplicateApplicationException;


// import pt.tecnico.mydrive.exception.NotRemovableException;

public class User extends User_Base {
    
    //User_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/
    // 		String username;
    // 		String password;
    // 		String name;
    // 		String mask;
    
// init functions //
    
    protected void init(MyDriveManager mydrive, String username, String name, String password, String mask) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
        Matcher matcher = pattern.matcher(username);
        
        if (!matcher.matches())
            throw new InvalidUsernameException(username, "it contains characters not accepted");
        if (username.length() < 3)
            throw new InvalidUsernameException(username, "it must have at least 3 characters");
        if (password.length() < 7)
            throw new PasswordLengthException(password, "it must have at least 8 characters");
        
        setUsername(username);
        setName(name);
        setPassword(password);
        setMask(mask);
        
        setMyDriveManager(mydrive);
        Directory home = new Directory(mydrive, this, mydrive.getHomeDirectory(), username);
        setHomeDirectory(home);
    }
    
// constructors //
    
    public User() { }
    
    public User(MyDriveManager mydrive, String username) {
        init(mydrive, username, username, username, "rwxd----");
    }
    
    public User(MyDriveManager mydrive, String username, String password) {
        init(mydrive, username, username, password, "rwxd----");
    }
    
    public User(MyDriveManager mydrive, String username, String name, String password) {
        init(mydrive, username, name, password, "rwxd----");
    }
    
    public User(MyDriveManager mydrive, String username, String name, String password, String mask) {
        init(mydrive, username, name, password, mask);
    }
    
    
// setters //
    
    @Override
    public void setMyDriveManager(MyDriveManager mydrive) {
        if (mydrive == null)
            super.setMyDriveManager(null);
        else
            mydrive.addUser(this);
    }
    
    
// functions //
	
	public boolean tokenNeverExpires() {
		return false;
	}
	
	public boolean hasReadPermission(File file) {
		if(this == file.getOwner()) {
			if(this.getMask().charAt(0) == 'r')				// make sure is r and not other char
				if(file.getPermissions().charAt(0) == 'r') {
					if(file instanceof Link)
						return hasReadPermission(((Link)file).getLinkedFile());
					return true;
				}
		} else {
			if(this.getMask().charAt(4) == 'r')
				if(file.getPermissions().charAt(4) == 'r') {
					if(file instanceof Link)
						return hasReadPermission(((Link)file).getLinkedFile());
					return true;
				}
		}
		
		return false;
	}
	
	public boolean hasWritePermission(File file) {
		if(this == file.getOwner()) {
			if(this.getMask().charAt(1) == 'w')				// make sure is w and not other char
				if(file.getPermissions().charAt(1) == 'w') {
					if(file instanceof Link)
						return hasWritePermission(((Link)file).getLinkedFile());
					return true;
				}
		} else {
			if(this.getMask().charAt(5) == 'w')
				if(file.getPermissions().charAt(5) == 'w') {
					if(file instanceof Link)
						return hasWritePermission(((Link)file).getLinkedFile());
					return true;
				}
		}
		
		return false;
	}
	
	public boolean hasExecutePermission(File file) {
		if(this == file.getOwner()) {
			if(this.getMask().charAt(2) == 'x')				// make sure is x and not other char
				if(file.getPermissions().charAt(2) == 'x') {
					if(file instanceof Link)
						return hasExecutePermission(((Link)file).getLinkedFile());
					return true;
				}
		} else {
			if(this.getMask().charAt(6) == 'x')
				if(file.getPermissions().charAt(6) == 'x') {
					if(file instanceof Link)
						return hasExecutePermission(((Link)file).getLinkedFile());
					return true;
				}
		}
		
		return false;
	}
	
	public boolean hasDeletePermission(File file) {
		if(this == file.getOwner()) {
			if(this.getMask().charAt(3) == 'd')				// make sure is d and not other char
				if(file.getPermissions().charAt(3) == 'd')
					return true;
		} else {
			if(this.getMask().charAt(7) == 'd')
				if(file.getPermissions().charAt(7) == 'd')
					return true;
		}
		
		return false;
	}
    
    public String toString() {
        String out = "";
        
        out += "Username: " + getUsername() + "\n"
        + "Name: " + getName() + "\n"
        + "Password: " + getPassword() + "\n"
        + "Mask: " + getMask() + "\n"
        + "Home: " + getHomeDirectory().getAbsolutePath() + "\n";
        
        return out;
    }
    
    public void printFileSet() {
        
        for(File file : getFileSet())
            System.out.println(file.getAbsolutePath());
        
    }

    public boolean validSessionTime(Session s){
    	DateTime aux = new DateTime();
    	return (aux.getMillis() - s.getDateOfLastAccess().getMillis()) < 7200000; // 2hours = 7,200,000ms
    }
    
    public void remove() {
        System.out.println("\n<- USER " + getUsername() + "\n");
        
        setMyDriveManager(null);
        setHomeDirectory(null);
        
        for (File file : getFileSet())
            file.remove();
        
        deleteDomainObject();
    }
    
    @Override
    public void addAssociation(Association assoc){
    	for(Association a : getAssociationSet()){
    		if( assoc.getExtension().equals(a.getExtension()) &&
    		   assoc.getApplication() == a.getApplication()){
    			throw new DuplicateAssociationException(this.getUsername(),assoc.getExtension(),assoc.getApplication().getName());
    		}
    		else if(assoc.getExtension().equals(a.getExtension())){
    			throw new DuplicateExtensionException(this.getUsername(),assoc.getExtension());
    		}
    		else if(assoc.getApplication() == a.getApplication()){
    			throw new DuplicateApplicationException(this.getUsername(),assoc.getApplication().getName());
    		}
    	}
    	super.addAssociation(assoc);
    	
    }
    
    public Application getAppFromExtension(String extension){
    	for(Association assoc : getAssociationSet()){
    		if(assoc.getExtension().equals(extension)){
    			return assoc.getApplication();
    		}
    	}
    	return null;
    }
    
    public String getExtensionFromApp(Application app){
    	for(Association assoc: getAssociationSet()){
    		if(assoc.getApplication() == app){
    			return assoc.getExtension();
    		}
    	}
    	return null;
    }
    
    public void xmlImport(Element userElement) {
        String name = getUsername();
        String mask = "rwxd----";
        String home = getUsername();
        
        if(userElement.getChild("name") != null)
            name = userElement.getChild("name").getValue();
        
        if(userElement.getChild("mask") != null)
            mask = userElement.getChild("mask").getValue();
        
        if(userElement.getChild("home") != null) {
            home = userElement.getChild("home").getValue();
            String[] names = home.split("/");
            home = names[names.length-1];
        }
        
       	setName(name);
       	setMask(mask);
        //getMyDriveManager().addUser(this);
        
        Directory homeDirectory = new Directory(getMyDriveManager(), this,  getMyDriveManager().getHomeDirectory(), home);
       	setHomeDirectory(homeDirectory);
    }
    
    public Element xmlExport() throws ExportException{
        try{
            Element element = new Element("user");
            Element password = new Element("password");
            Element name = new Element("name");
            Element mask = new Element("mask");
            Element home = new Element("home");
            
            
            element.setAttribute("username", getUsername());
            password.setText(getPassword());
            name.setText(getName());
            mask.setText(getMask());
            home.setText(getHomeDirectory().getAbsolutePath());
            
            
            element.addContent(password);
            element.addContent(name);
            element.addContent(mask);
            element.addContent(home);
            
            return element;
        } catch(IllegalNameException ex) {
            throw new ExportException("user",getUsername(),ex.getMessage());
        }
    }
}


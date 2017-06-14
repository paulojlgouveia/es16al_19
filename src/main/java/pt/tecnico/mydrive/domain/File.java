package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.xml.*;
import pt.tecnico.mydrive.exception.permission.WritePermissionException;
import pt.tecnico.mydrive.exception.file.creation.PathTooLongException;
import pt.tecnico.mydrive.exception.file.creation.DuplicateFileException;
import pt.tecnico.mydrive.exception.file.access.NotDirectoryException;
import pt.tecnico.mydrive.exception.file.access.NotEditableException;
import pt.tecnico.mydrive.exception.file.access.NotExecutableException;
import pt.tecnico.mydrive.exception.file.access.NotRemovableException;
import pt.tecnico.mydrive.exception.user.creation.DuplicateUsernameException;
import pt.tecnico.mydrive.exception.file.access.NotReadableException;

public abstract class File extends File_Base {
    
//File_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/
// 		Integer id;
// 		String name;
// 		String permissions;
// 		DateTime lastModifiedDate;

    
// init functions //

	protected void init(int id, User owner, Directory parent, String name,String extension)
				throws DuplicateFileException, WritePermissionException, PathTooLongException {
		
		if(parent == null) {	// create base directory
			if(owner.getUsername().equals("root")) {
				if(name.length() > 1024)
					throw new PathTooLongException(name, name.length());
					
				setId(id);
				setName(name);
				setOwner(owner);
				setParentDirectory((Directory) this);
				setPermissions(owner.getMask());
				DateTime date = new DateTime();
				setLastModifiedDate(date);
				setExtension(extension);
				
			} else {
				
			}
		} else {
			if(parent.getAbsolutePath().equals("/home") && name.equals(owner.getUsername())) {
				if((name.length()+parent.getAbsolutePath().length()) > 1024)
					throw new PathTooLongException(name, (name.length()+parent.getAbsolutePath().length()));
				
				setId(id);
				setName(name);
				setOwner(owner);
				setParentDirectory(parent);
				setPermissions(owner.getMask());
				DateTime date = new DateTime();
				setLastModifiedDate(date);
				setExtension(extension);
				
			} else {
				if(!owner.hasWritePermission(parent)) {	// user cant write in parent directory
					throw new WritePermissionException(owner.getUsername(), parent.getAbsolutePath());
					
				} else {
					if(parent.getEntry(name) != null)	// file exists
						throw new DuplicateFileException(name);
					
					if((name.length()+parent.getAbsolutePath().length()) > 1024)
						throw new PathTooLongException(name, (name.length()+parent.getAbsolutePath().length()));
					
					setId(id);
					setName(name);
					setOwner(owner);
					setParentDirectory(parent);
					setPermissions(owner.getMask());
					DateTime date = new DateTime();
					setLastModifiedDate(date);
					setExtension(extension);
				}
			}
		}
	}
	
// constructors //

	public File() {}
	
	public File(MyDriveManager mydrive, User owner, Directory parent, String name) {
		this.init(mydrive.generateId(), owner, parent, name,"");
	}

// functions //

	public String getAbsolutePath() {
		String path = getName();
		
		if(path.equals("/"))
			return path;
		
		for(File dir=getParentDirectory(); !dir.getName().equals("/"); dir=dir.getParentDirectory())
			path = dir.getName() + "/" + path;
		
		return "/" + path;
	}
	
	
	// public File getFileByPath(String path) {
	// 	if(path.charAt(0) == '/')
	// 		return getFileByAbsolutePath(path);
	// 	else
	// 		return getFileByRelativePath(path);
	// }
	
	// public File getFileByRelativePath(String path) {
	// 	File file = getParentDirectory();
	// 	String[] folders = path.split("/");
					
	// 	for(int t=1; t<folders.length; t++) {
	// 		file = file.getEntry(folders[t]);
			
	// 		if(file == null)
	// 			return null;	// replace with something else, maybe invalidpathexception
	// 	}
	// 	return file;
	// }
	
	// public File getFileByAbsolutePath(String path) {
	// 	if(path.charAt(0) == '/') {
	// 		File file = MyDriveManager.getInstance().getBaseDirectory();
	// 		String[] folders = path.split("/");
						
	// 		for(int t=1; t<folders.length; t++) {
	// 			file = file.getEntry(folders[t]);
				
	// 			if(file == null)
	// 				return null;	// replace with something else, maybe invalidpathexception
	// 		}
	// 		return file;
	// 	}
	// 	return null;	// replace with something else more representative of the error
	// }

	public void remove() throws NotRemovableException {
		String fullpath = getAbsolutePath();
		
		if(fullpath.equals("/") || fullpath.equals("/home"))
			throw new NotRemovableException(this.getAbsolutePath());
			
		setOwner(null);
		setParentDirectory(null);
		deleteDomainObject();
    }
    
    
// leaf functions //

	public abstract int getSize();
	
	public abstract String list() throws NotDirectoryException;
	
	public abstract File getEntry(String name) throws NotDirectoryException;
	
	public abstract void setContent(String content);
	
	public abstract String getContent();

	public abstract String readContent() throws NotReadableException;
	
	public abstract void writeContent(String content) throws NotEditableException;
	
	public abstract void execute(MyDriveManager mydrive, String[] args);
	
	
	
// xml //

	public abstract void xmlImport(MyDriveManager md, Element element);


	public void xmlFileImport(MyDriveManager md, Element element) {
 		String path, name, owner, perm;
 
         if(element.getChild("name") != null){
         	name = element.getChild("name").getValue();
         	setName(name);
         }
 
         if(element.getChild("owner") != null){
 	        owner = element.getChild("owner").getValue();
 	     	try{
 	     		User user = md.getUserByUsername(owner);
 	     		setOwner(user);
 	     	}
 	        catch (DuplicateUsernameException e){
 	        	System.out.println(e.getMessage());
 	        }
         }
 
         if(element.getChild("path") != null){
         	path = element.getChild("path").getValue();
         	String[] names = path.split("/");

        	File dir = md.getHomeDirectory();
        	
        	for(int t=1; t<names.length; t++) {
				if(dir.getEntry(names[t]) == null)
					new Directory(md, md.getUserByUsername("root"),(Directory) dir, names[t]);

				dir = dir.getEntry(names[t]);
				
			}

			setParentDirectory((Directory) dir);
         }
 
 	    if(element.getChild("perm") != null){
         	perm = element.getChild("perm").getValue();
         	setPermissions(perm);
 	    }
	}
	
	public abstract Element xmlExport() throws ExportException;
	
	public Element xmlFileExport() throws IllegalNameException{
		try{
			Element element = new Element("temp");
			Element name = new Element("name");
			Element path = new Element("path");
			Element owner = new Element("owner");
			Element perm = new Element("perm");
			
			element.setAttribute("id", getId().toString());
	        name.setText(getName());
	        owner.setText(getOwner().getName());
	        path.setText(getAbsolutePath());
	        perm.setText(getPermissions());
	        
	        element.addContent(path);
	        element.addContent(owner);
	        element.addContent(name);
	        element.addContent(perm);
	        
	        return element;
		}
		catch(IllegalNameException ine){
			throw ine;
		}
	}

}

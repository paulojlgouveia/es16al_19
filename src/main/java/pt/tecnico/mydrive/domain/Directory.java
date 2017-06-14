package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;

import pt.tecnico.mydrive.exception.file.access.NotEditableException;
import pt.tecnico.mydrive.exception.file.access.NotExecutableException;
import pt.tecnico.mydrive.exception.file.access.NotRemovableException;
import pt.tecnico.mydrive.exception.file.access.NotReadableException;

import pt.tecnico.mydrive.exception.xml.*;


public class Directory extends Directory_Base {

// init functions //

	
// constructors //

	public Directory() {}
	
	public Directory(MyDriveManager mydrive, User owner, Directory parent, String name) {
		super.init(mydrive.generateId(), owner, parent, name,"");
	}
	
	public Directory(MyDriveManager mydrive, User owner, Directory parent, String name,String extension) {
		super.init(mydrive.generateId(), owner, parent, name,extension);
	}
	
	public Directory(MyDriveManager mydrive, User owner, String name) {

		if(!owner.getUsername().equals("root") && mydrive.getBaseDirectory()==null) {
			throw new NotRemovableException(this.getAbsolutePath());
			
		} else {
			if(name == "/") {
				super.init(mydrive.generateId(), owner, null, name,"");
				mydrive.setBaseDirectory(this);
			}
			
			if(name == "home") {
				super.init(mydrive.generateId(), owner, mydrive.getBaseDirectory(), name,"");
			}
			
		}
		
	}

	
// functions //

	@Override
	public void remove() throws NotRemovableException {
	
		for(File file : getFileSet())
			file.remove();
		
		super.remove();
    }
    
	

// leaf functions //

 	@Override
	public int getSize() {
		int size = 2;
		for(File file : getFileSet())
				size++;
		
		return size;
	}
	
	@Override
	public String list() {
		String out = ".\n..\n";
		String filename  = "";
				
		for(File file : getFileSet()) {
			filename = file.getName();
			
			if(!filename.equals("/"))
				out = out + filename + "\n";
		}
		
		return out;
	}
	
	@Override
	public File getEntry(String name) {
	
		if(!name.equals("/")) {
			if(name.equals("."))	return this;
			if(name.equals(".."))	return getParentDirectory();
			
			for(File file : getFileSet()) {
				if(file.getName().equals(name))
					if(file != this && file != getParentDirectory())	// don't think is needed
						return file;
			}
		}
		return null;
	}

	public File getFileByName(String name){
		File f = null;
		for(File file : getFileSet()){
			if(file.getName().equals(name))
				return file;
			if(file instanceof Directory){
				Directory dir = (Directory) file;
				f = dir.getFileByName(name);
			}
			if(f != null)
				return f;
		}
		return null;
	}
	
 	@Override
	public String getContent() throws NotEditableException {
		throw new NotReadableException(this.getAbsolutePath());
	}
		
 	@Override
	public void setContent(String newcontent) throws NotEditableException {
		throw new NotEditableException(this.getAbsolutePath());
	}

	@Override
	public String readContent() throws NotReadableException {
		throw new NotReadableException(this.getAbsolutePath());
	}
	
	@Override
	public void writeContent(String content) throws NotEditableException {
		throw new NotEditableException(this.getAbsolutePath());
	}
	
 	@Override
	public void execute(MyDriveManager mydrive, String[] args){
 		User owner = getOwner();
		String extension = getExtension();
		
		if(!extension.equals("")){
			(owner.getAppFromExtension(extension)).execute(mydrive,args);
		}
	}

	@Override
	public void setExtension(String extension){
		super.setExtension(extension);
	}
	

// xml //
	 
	@Override
	public void xmlImport(MyDriveManager md, Element element){
		xmlFileImport(md, element);
	}
	 
 	@Override
	public Element xmlExport() throws ExportException{
		try{
    		Element element = xmlFileExport();
    		element.setName("dir");
    		
    		return element;
    	}
    	catch(IllegalNameException ex){
    		throw new ExportException("directory",getName(),ex.getMessage());
    	}
	}

}



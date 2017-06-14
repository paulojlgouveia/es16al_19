package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;

import pt.tecnico.mydrive.exception.xml.*;
import pt.tecnico.mydrive.exception.file.InvalidLinkException;

import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.file.access.NotDirectoryException;

public class Link extends Link_Base{

// init functions //

	protected void init(int id, User owner, Directory parent, String name, String content) {
		super.init(id, owner, parent, name, content);
	}


// constructors //
	
	public Link() {}
	
	public Link(MyDriveManager mydrive, User owner, Directory parent, String name, String content) {
// 		if(mydrive.getFileByAbsolutePath(content) == null)
// 			throw new InvalidLinkException(name, content);
			
		init(mydrive.generateId(), owner, parent, name, content);
	}
	
// aux functions //

	protected File getLinkedFile() throws InvalidLinkException {
		MyDriveManager mydrive = MyDriveManager.getInstance();
		System.out.println(getParentDirectory().getName());
		File file = mydrive.getFileByPath(getParentDirectory(), getContent());
		if (file == null)
			throw new InvalidLinkException(getContent());
		
		return file;
	}
	
// leaf functions //

	@Override
 	public String list() throws NotDirectoryException {
		return getLinkedFile().list();
	}

	@Override
	public File getEntry(String name) throws NotDirectoryException {
		return getLinkedFile().getEntry(name);
	}

	@Override
	public String readContent(){
		return getLinkedFile().readContent();
	}

	@Override
	public void writeContent(String content) {
		getLinkedFile().setContent(content);
	}

	@Override

	public void execute(MyDriveManager mydrive, String[] args) {
		User owner = getOwner();

		getLinkedFile().execute(mydrive,args);
	}
	


// xml //
	@Override
	public void xmlImport(MyDriveManager md, Element element){
		super.xmlImport(md, element);
		
		if(element.getChild("value") != null){
        	String value = element.getChild("value").getValue();
			
        	setContent(value);
		}
	}
	
	@Override
    public Element xmlExport() throws ExportException{
    	try{
    		Element element = xmlFileExport();
    		Element value = new Element("value");
    		
    		element.setName("link");
    		value.setText(getContent());
    		
    		element.addContent(value);
    		
    		return element;
    	}
    	catch(IllegalNameException ine){
    		throw new ExportException("link",getName());
    	}
   }
}

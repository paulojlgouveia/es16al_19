package pt.tecnico.mydrive.domain;

import java.util.*;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;

import pt.tecnico.mydrive.exception.file.access.NotDirectoryException;
import pt.tecnico.mydrive.exception.file.access.NotExecutableException;

import pt.tecnico.mydrive.exception.xml.*;


public class PlainFile extends PlainFile_Base {

//PlainFile_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/
// 		String content;


// init functions //
	
	protected void init(int id, User owner, Directory parent, String name, String content) {
		this.setContent(content);
		super.init(id, owner, parent, name, "");
	}
	
// constructors //

	public PlainFile() {}
	
	public PlainFile(MyDriveManager mydrive, User owner, Directory parent, String name) {
		this.init(mydrive.generateId(), owner, parent, name, "");
	}
	
	public PlainFile(MyDriveManager mydrive, User owner, Directory parent, String name, String content) {
		this.init(mydrive.generateId(), owner, parent, name, content);
	}

	
// leaf functions //

	@Override
	public int getSize() {
		return getContent().length();
	}
	
	@Override
 	public String list() throws NotDirectoryException {
		throw new NotDirectoryException(this.getAbsolutePath());
	}
	
 	@Override
	public File getEntry(String name) throws NotDirectoryException {
		throw new NotDirectoryException(this.getAbsolutePath());
	}
 	
	// @Override
	// public String getContent() /*throws NotEditableException*/ {
	// 	return super.getContent();
	// }
 	
	// @Override
	// public void setContent(String newcontent) {
	// 	super.setContent(newcontent);
	// }

	@Override
	public String readContent(){
		return getContent();
	}
	
	@Override
	public void writeContent(String content){
		setContent(content);
	}

 	@Override
	public void execute(MyDriveManager mydrive, String[] args) {
 		String path;
 		File file;
		String[] lines = getContent().split("\n");
		String[] invocation;
		String[] aux;
		
		for(String line : lines){
			invocation = line.split(" ");
			
			if(invocation[0].charAt(0) != '/'){
				path = getAbsolutePath() + "/" + invocation[0];
			}
			else{
				path = invocation[0];
			}
			
			file = mydrive.getFileByAbsolutePath(path);
			file.execute(mydrive,Arrays.copyOfRange(invocation,1,invocation.length));
		}
	}
	
// xml //
	@Override
	public void xmlImport(MyDriveManager md, Element element){
		xmlFileImport(md, element);

		if(element.getChild("contents") != null){
        	String content = element.getChild("contents").getValue();

        	setContent(content);
		}
	}
	
	public Element xmlExport() throws ExportException{
		try{
    		Element element = xmlFileExport();
    		Element contents = new Element("contents");
    		
    		element.setName("plain");
    		contents.setText(getContent());
    		
    		element.addContent(contents);
    		return element;
    	}
    	catch(IllegalNameException ine){
    		throw new ExportException("plainFile",getName());
    	}
	}
}

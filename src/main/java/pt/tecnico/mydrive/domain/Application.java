package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.IllegalNameException;

import java.lang.reflect.*;

import pt.tecnico.mydrive.exception.xml.*;
import pt.tecnico.mydrive.exception.file.execution.InvalidClassException;
import pt.tecnico.mydrive.exception.file.execution.InvalidMethodException;
import pt.tecnico.mydrive.exception.file.execution.FileExecutionException;

public class Application extends Application_Base {

//Application_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/


// init functions //

	protected void init(int id, User owner, Directory parent, String name, String content) {
		super.init(id, owner, parent, name, content);
	}

	
// constructors //

	public Application() {}
	
	public Application(MyDriveManager mydrive, User owner, Directory parent, String name) {
		init(mydrive.generateId(), owner, parent, name, "");
	}
	
	public Application(MyDriveManager mydrive, User owner, Directory parent, String name, String content) {
		init(mydrive.generateId(), owner, parent, name, content);
	}
    
// leaf functions //
	
	@Override
	public void execute(MyDriveManager mydrive, String[] args){
		
		String className = getFullClassName();
	    String methodName = getMethodName();
		
		try{
			run(args);

		}
		catch (ClassNotFoundException cnfe){
			throw new InvalidClassException(this,className);
		}
		catch (NoSuchMethodException nsme){
				throw new InvalidMethodException(this,methodName);
		}
		catch(ReflectiveOperationException iae){
			throw new FileExecutionException(this.getAbsolutePath());
		}

		catch (RuntimeException re){
			throw re;
		}
		
	}
	
	public void run (String[] args) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> cls;
	    Method meth;
	    String className = getFullClassName();
	    String methodName = getMethodName();
	  
	    
		System.out.println(className);
		System.out.println(methodName);
		
		
		cls = Class.forName(className);
		meth = cls.getMethod(methodName, String[].class);
		
	    meth.invoke(null, (Object)args);
	}
	
	public String getFullClassName(){
		String className = "";
		int endIndex;
		String[] aux = getContent().split("\\.");
		
		
		if(aux[aux.length - 1].endsWith("()")){
			endIndex = aux.length - 1;
		}
		else{
			endIndex = aux.length;
		}
		
		for(int i = 0; i < endIndex;i++){
			if(i == endIndex - 1){
				className += aux[i];
			}
			else{
				className += aux[i] + ".";
			}
		}
		return className;
	}
	
	public String getMethodName(){
		String[] aux = getContent().split("\\.");
		
		if(aux[aux.length - 1].endsWith("()")){
			return aux[aux.length - 1].substring(0,aux[aux.length - 1].length() - 2);
		}
		else{
			return "main";
		}
	}
	
// xml //

	@Override
	public void xmlImport(MyDriveManager md, Element element){
		super.xmlImport(md, element);

		if(element.getChild("method") != null){
        	String method = element.getChild("method").getValue();

        	setContent(method);
		}
	}
	
	@Override
    public Element xmlExport() throws ExportException{
    	try{
    		Element element = xmlFileExport();
    		Element method = new Element("method");
    		
    		element.setName("app");
    		method.setText(getContent());
    		
    		element.addContent(method);
    		
    		return element;
    	}
    	catch(IllegalNameException ine){
    		throw new ExportException("application",getName());
    	}
   }
}

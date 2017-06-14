package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.jdom2.Document;
import org.jdom2.DataConversionException;

import pt.ist.fenixframework.FenixFramework;

import java.util.TreeMap;

import pt.tecnico.mydrive.exception.xml.*;
import pt.tecnico.mydrive.exception.user.UserIsNotInSessionException;
import pt.tecnico.mydrive.exception.user.creation.DuplicateUsernameException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;


// import pt.tecnico.mydrive.exception.*;


public class MyDriveManager extends MyDriveManager_Base {
	
//MyDrive_Base target/generated-sources/dml-maven-plugin/pt/tecnico/mydrive/domain/
// 		int Counter;
// 		User CurrentUser;
// 		User[] UserSet;
// 		Directory RootDirectory;

// static domain object //

	public static MyDriveManager getInstance() {
		MyDriveManager mydrive = FenixFramework.getDomainRoot().getMyDriveManager();
		
		if (mydrive != null) { return mydrive; }
		
		return new MyDriveManager();
	}
	

// constructors //

	private MyDriveManager() {
// 		System.out.println("\n -> MYDRIVE \n");

		setRoot(FenixFramework.getDomainRoot());
		Integer counter = new Integer(0);
		setCounter(counter);
		
		new SuperUser(this);
		new GuestUser(this);
	}

	
// getters //

	public Directory getHomeDirectory() {
		return (Directory) getBaseDirectory().getEntry("home");
	}
	
	public File getFileByAbsolutePath(String path) {
		if(path.charAt(0) == '/') {
			File file = getBaseDirectory();
			String[] folders = path.split("/");
						
			for(int t=1; t<folders.length; t++) {
				file = file.getEntry(folders[t]);
				
				if(file == null)
					return null;	// replace with something else, maybe invalidpathexception
			}
			return file;
		}
		return null;	// replace with something else more representative of the error
	}
	
	public File getFileByRelativePath(File startDir, String path) {
		File file = startDir;
		String[] folders = path.split("/");
					
		for(int t=0; t<folders.length; t++) {
			file = file.getEntry(folders[t]);
			
			if(file == null)
				return null;	// replace with something else, maybe invalidpathexception
		}
		return file;
	}
	
	public File getFileByPath(File startDir, String path) {
		if(path.charAt(0) == '/')
			return getFileByAbsolutePath(path);
		return getFileByRelativePath(startDir, path);
	}
	
	public File getFileByName(String name){
		File f = null;
		for(File file : getBaseDirectory().getFileSet()) {
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
	
	public User getUserByUsername(String username) {
		for (User user : getUserSet())
			if (user.getUsername().equals(username))
				return user;
			
		return null;
	}
	
	public Session getSessionByToken(long token) {
		for (Session session : getSessionSet())
			if (session.getToken() == token)
				return session;

		return null;
	}
	
	public long getTokenByUsername(String username) {
		for (Session session : getSessionSet())
			if (session.getUser().getUsername().equals(username))
				return session.getToken();
		
		throw new UserIsNotInSessionException(username);
	}
	
	public User getUserByToken(long token) {
		Session session = getSessionByToken(token);
		if (session == null)
			throw new InvalidTokenException(token);
			
		return session.getUser();
	}
		
// setters //

	@Override
	public void addUser(User userToBeAdded) throws DuplicateUsernameException {
		if(getUserByUsername(userToBeAdded.getUsername()) != null)
			throw new DuplicateUsernameException(userToBeAdded.getUsername());
			
		super.addUser(userToBeAdded);
	}
	
	
// functions //

	public Integer generateId () {
		Integer aux = getCounter();
		setCounter(new Integer(aux.intValue() + 1));
		
		return aux;
	}
	
	public void listUsers() {
		for (User user : getUserSet())
			System.out.println(user.toString());
	}
	
	
	//FIXME REMOVE THIS FUNCTION BEFORE SUBMISSION
	public void listSessions() {
    	System.out.println("\nList of Sessions:");
		for (Session session : getSessionSet())
			System.out.println(session.toString());
	}
	
	public void removeInvalidSessions() {
		for (Session session : getSessionSet()) {
			if (!session.isValid() || session.getUser().tokenNeverExpires())
				session.remove();
		}
	}
	
  	public void cleanup() {
		setCounter(new Integer(0));
	
		for (Session session : getSessionSet())
	    	session.remove();
	    	
        for (User user : getUserSet()) {
			try{
				user.remove();
			} catch (Exception e) { System.out.println(e.getMessage()); }
	    }
    }
    

// xml //

	public void xmlImport(Element element) {
        for (Element node: element.getChildren("user")) {
            String username = node.getAttribute("username").getValue();
            
            if (getUserByUsername(username) == null){
            		String password = username;
            		if(node.getChild("password") != null)
            			password = node.getChild("password").getValue();

	                User user = new User(this, username, password);
	                //user.setMyDriveManager(this);
	                user.xmlImport(node);
	             }

	    }

         for (Element node: element.getChildren("dir")){
         	try{
         		int id = node.getAttribute("id").getIntValue();

         		Directory dir = new Directory();
         		dir.setId(id);
         		dir.xmlImport(this, node);
         	}
         	catch (DataConversionException dce){
         		System.out.println(dce.getMessage());
         	}
         	//TEST IF ID IS UNIQUE
         }

         for (Element node: element.getChildren("plain")){
         	try{
         		int id = node.getAttribute("id").getIntValue();

         		PlainFile pf = new PlainFile();
         		pf.setId(id);
         		pf.xmlImport(this, node);
         	}
         	catch (DataConversionException dce){
         		System.out.println(dce.getMessage());
         	}
         }

         for (Element node: element.getChildren("link")){
         	try{
         		int id = node.getAttribute("id").getIntValue();

         		Link link = new Link();
         		link.setId(id);
         		link.xmlImport(this, node);
         	}
         	catch (DataConversionException dce){
         		System.out.println(dce.getMessage());
         	}
         }

         for (Element node: element.getChildren("app")){
         	try{
         		int id = node.getAttribute("id").getIntValue();

         		Application app = new Application();
         		app.setId(id);
         		app.xmlImport(this, node);
         	}
         	catch (DataConversionException dce){
         		System.out.println(dce.getMessage());
         	}
         }
	}
	
  	public Document xmlExport() throws ExportException {
		try {
			TreeMap<Integer, File> xmlFiles = new TreeMap<Integer, File>();
			Element element = new Element("mydrive");
			Element counter = new Element("counter");

			counter.setText(getCounter().toString());
			element.addContent(counter);
			for (User u : getUserSet()) {
				if (!u.getName().equals("Super User")) {
					element.addContent(u.xmlExport());
				}
				for (File f : u.getFileSet()) {
					String auxName = f.getName();
					String auxPath = f.getAbsolutePath();
					if(!auxName.equals("/") && !auxPath.equals("/home") && !auxPath.equals("/home/root")){
						xmlFiles.put(f.getId(), f);
					}
				}
			}
			for(File f : xmlFiles.values()){
				element.addContent(f.xmlExport());
			}

			Document doc = new Document(element);
			return doc;
		} catch (ExportException ee) {
			throw new ExportException("mydrive", ee.getMessage());
		}
	}

	
	
}

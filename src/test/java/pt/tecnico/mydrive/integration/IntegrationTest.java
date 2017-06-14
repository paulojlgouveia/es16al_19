package pt.tecnico.mydrive.integration;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.service.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class IntegrationTest extends AbstractServiceTest {

	private final String USERNAME = "mario";
	private final String NAME = "Mario";
	private final String PASSWORD = "mushroom";
	private final String MASK = "rwxd----";
	private final String HOMEDIR = "/home/mario";
	
	private final String ROOT_USERNAME = "root";
	private final String ROOT_PASSWORD = "***";

	private final String DIR_NAME = "my_dir";
	private final String FILE_NAME = "my_file";
	private final String APP_NAME = "my_app";
	private final String LINK_NAME = "my_link";
	private final String VAR_NAME = "my_var";
	
	private final String FILE_CONTENT = "This is a text sample";
	private final String APP_CONTENT = "pt.tecnico.mydrive.integration.IntegrationTest.myMethod1()";
	private final String NEW_APP_CONTENT = "pt.tecnico.mydrive.integration.IntegrationTest.myMethod2()";
	private final static String VAR_VALUE = "This is a variable value sample";
	private final static String NEW_VAR_VALUE = "This is a different variable value sample";

	private MyDriveManager mydrive;
	private long token;
	private static String testVar;

    public static void myMethod1(String [] args){
    	testVar = VAR_VALUE;
    }
    
    public static void myMethod2(String [] args){
    	testVar = NEW_VAR_VALUE;
    }
    
    protected void populate() {
		mydrive = MyDriveManager.getInstance();
		
		new User(mydrive, USERNAME, NAME, PASSWORD, MASK);
    }
    
    // TEST SUMMARY:
    // 	LoginUserService........................(2/2)
    //		login as simple user................OK
    //		login as root.......................OK
    // 	CreateFile..............................(4/4)
    //		create directory....................OK
    //		create text file....................OK
    //		create application..................OK
    //		create link.........................OK
    // 	ListDirectory...........................(2/2)
    //		after file creation.................OK
    //		after file deletion.................OK
    // 	ChangeDir...............................(3/3)
    //		enter owned dir.....................OK
    //		enter not owned dir (as root).......OK
    //		exit to parent dir..................OK
    // 	WriteFile...............................(3/3)
    //		write text file.....................OK
    //		write application...................OK
    // 		write link..........................missing
    // 	ReadFile................................(3/3)
   	//		read text file......................OK
   	//		read application....................OK
   	//		read link...........................OK
    // 	ExecuteFile.............................(2/2)
    //		execute application.................OK
    //		execute link (to application).......OK
    // 	DeleteFile..............................(2/2)
    //		delete link.........................OK
    //		delete directory (with content).....OK
    // 	AddVariable.............................(2/2)
    //		add variable........................OK
	//		change existing variable............OK
	
    @Test
    public void success() throws Exception {
    	
    	// login as simple user [LoginUserService]
		LoginUserService login = new LoginUserService(USERNAME, PASSWORD);
		login.execute();
		token = login.result();
		
		// create new directory [CreateFileService]
		new CreateFileService(token, FileType.DIRECTORY, DIR_NAME).execute();
		
		// enter directory [ChangeDirectoryService]
		ChangeDirectoryService changeDir = new ChangeDirectoryService(token, DIR_NAME);
		changeDir.execute();
		assertEquals("Current directory is wrong", HOMEDIR + "/" + DIR_NAME, changeDir.result());
		
		// create new text file and application [CreateFileService]
		new CreateFileService(token, FileType.PLAINFILE, FILE_NAME).execute();
		new CreateFileService(token, FileType.APPLICATION, APP_NAME).execute();
		
		// list directory [ListDirectoryService]
		ListDirectoryService listDir = new ListDirectoryService(token);
		listDir.execute();
		assertEquals("Number of files listed is wrong", 4, listDir.result().size());
		
		// change text file and application contents [WriteFileService]
		new WriteFileService(token, FILE_NAME, FILE_CONTENT).execute();
		new WriteFileService(token, APP_NAME, APP_CONTENT).execute();
		
		// read text file and application contents [ReadFileService]
		ReadFileService readFile = new ReadFileService(token, FILE_NAME);
		readFile.execute();
		assertEquals("Text file contents are wrong", FILE_CONTENT, readFile.result());
		
		readFile = new ReadFileService(token, APP_NAME);
		readFile.execute();
		assertEquals("Application contents are wrong", APP_CONTENT, readFile.result());
		
		// execute application [ExecuteFileService]
		ExecuteFileService executeFile = new ExecuteFileService(token, APP_NAME, null);
		executeFile.execute();
		assertEquals("Application didn't have the expected results", VAR_VALUE, testVar);
		
		// exit directory [ChangeDirectoryService]
		changeDir = new ChangeDirectoryService(token, "..");
		changeDir.execute();
		assertEquals("Current directory is wrong", HOMEDIR, changeDir.result());
		
		// create link to application [CreateFileService]
		new CreateFileService(token, FileType.LINK, LINK_NAME, HOMEDIR + "/" + DIR_NAME + "/" + APP_NAME).execute(); //FIXME
		
		// login as root [LoginUserService]
		login = new LoginUserService(ROOT_USERNAME, ROOT_PASSWORD);
		login.execute();
		token = login.result();
		
		// enter user homedir [ChangeDirectoryService]
		changeDir = new ChangeDirectoryService(token, HOMEDIR);
		changeDir.execute();
		assertEquals("Current directory is wrong", HOMEDIR, changeDir.result());
		
		// change link contents [WriteFileService]
		new WriteFileService(token, LINK_NAME, NEW_APP_CONTENT).execute();
		
		// read link contents [ReadFileService]
		readFile = new ReadFileService(token, LINK_NAME);
		readFile.execute();
		assertEquals("Link contents are wrong", NEW_APP_CONTENT, readFile.result());
		
		// execute link [ExecuteFileService]
		executeFile = new ExecuteFileService(token, LINK_NAME, null);
		executeFile.execute();
		assertEquals("Application didn't have the expected results", NEW_VAR_VALUE, testVar);

		// delete link [DeleteFileService]
		new DeleteFileService(token, LINK_NAME).execute();
	
		// delete directory [DeleteFileService]
		new DeleteFileService(token, DIR_NAME).execute();
	
		// list directory [ListDirectoryService]
		listDir = new ListDirectoryService(token);
		listDir.execute();
		assertEquals("Number of files listed is wrong", 2, listDir.result().size());
		
		// add environment variable [AddVariableService]
		AddVariableService addVar = new AddVariableService(token, VAR_NAME, VAR_VALUE);
		addVar.execute();
		assertEquals("Number of variables set is wrong", 1, addVar.result().size());
		assertEquals("Variable value is wrong", VAR_NAME + "=" + VAR_VALUE, addVar.result().get(0));
		
		// change environment variable value [AddVariableService]
		addVar = new AddVariableService(token, VAR_NAME, NEW_VAR_VALUE);
		addVar.execute();
		assertEquals("Number of variables set is wrong", 1, addVar.result().size());
		assertEquals("Variable value is wrong", VAR_NAME + "=" + NEW_VAR_VALUE, addVar.result().get(0));
    }
}
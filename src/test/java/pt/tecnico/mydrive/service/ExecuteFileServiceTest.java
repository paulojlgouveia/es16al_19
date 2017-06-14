package pt.tecnico.mydrive.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import pt.tecnico.mydrive.domain.Application;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.file.execution.InvalidClassException;
import pt.tecnico.mydrive.exception.file.execution.InvalidMethodException;
import pt.tecnico.mydrive.exception.permission.ExecutePermissionException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

public class ExecuteFileServiceTest extends AbstractServiceTest {

	private MyDriveManager mydrive;
	private User testUser;
	private User otherUser;
	private Session userSession;
	private Session otherSession;
	private String[] args = { "number" };
	private String[] args2 = {};
	private long userToken;
	private long otherToken;
	private long invalidToken;

	public static void functionTest(String[] args) { }

	@Override
	protected void populate() {
		mydrive = MyDriveManager.getInstance();
		testUser = new User(mydrive, "test", "Test User", "t35t....", "rwxd----");
		otherUser = new User(mydrive, "otherTest", "Test User2", "t35t....", "xxxx----");
		userSession = new Session(mydrive, "test", "t35t....");
		otherSession = new Session(mydrive, "otherTest", "t35t....");
		userToken = userSession.getToken();
		otherToken = otherSession.getToken();
		invalidToken = -3212680428774279330L;

		new Application(mydrive, testUser, userSession.getCurrentDirectory(), "Application",
				"pt.tecnico.mydrive.service.ExecuteFileServiceTest.functionTest()");
		assertNotNull("hello app not created", mydrive.getFileByAbsolutePath("/home/test/Application"));
		
		new Application(mydrive, testUser, userSession.getCurrentDirectory(), "ApplicationWrongMethod",
				"pt.tecnico.mydrive.service.ExecuteFileServiceTest.wrongfunctionTest()");
		assertNotNull("hello app not created", mydrive.getFileByAbsolutePath("/home/test/ApplicationWrongMethod"));
		
		new PlainFile(mydrive, testUser, userSession.getCurrentDirectory(), "plainfile");
		assertNotNull("plainfile not created", mydrive.getFileByAbsolutePath("/home/test/plainfile"));

		Directory dir1 = new Directory(mydrive, testUser, userSession.getCurrentDirectory(), "dir");
		assertNotNull("directory not created", mydrive.getFileByAbsolutePath("/home/test/dir"));

		new Application(mydrive, testUser, dir1, "newApp");
		assertNotNull("app not created", mydrive.getFileByAbsolutePath("/home/test/dir/newApp"));

		Directory dir2 = new Directory(mydrive, testUser, dir1, "subdir");
		assertNotNull("directory not created", mydrive.getFileByAbsolutePath("/home/test/dir/subdir"));

		new Link(mydrive, testUser, dir2, "link", "/home/test");
		assertNotNull("link not created", mydrive.getFileByAbsolutePath("/home/test/dir/subdir/link"));

	}

	// Tests with Success

	@Test
	public void ExecuteApplicationSuccess() {
		 ExecuteFileService service = new ExecuteFileService(userToken, "/home/test/Application", args);
		 service.execute();
		
		 assertNotNull("application was not executed", mydrive.getFileByAbsolutePath("/home/test/Application"));
	}

	@Test
	public void ExecuteLinkSuccess() {
		 ExecuteFileService service = new ExecuteFileService(userToken, "/home/test/dir/subdir/link", args);
		 service.execute();
		
		 assertNotNull("link was not executed", mydrive.getFileByAbsolutePath("/home/test/dir/subdir/link"));
	}

	// More tests will be added here

	// Tests without Success

	@Test(expected = InvalidTokenException.class)
	public void ExecuteFileWithInvalidToken() {
		ExecuteFileService service = new ExecuteFileService(invalidToken, "somePath", args);
		service.execute();
	}

	@Test(expected = ExecutePermissionException.class)
	public void ExecuteFileWithoutPermissions() {
		ExecuteFileService service = new ExecuteFileService(otherToken, "/home/test/Application", args);
		service.execute();
	}
	
	@Test(expected = InvalidMethodException.class)
	public void ExecuteApplicationWithInvalidMethod() {
		 ExecuteFileService service = new ExecuteFileService(userToken, "/home/test/ApplicationWrongMethod", args);
		 service.execute();
		
		 assertNotNull("application was not executed", mydrive.getFileByAbsolutePath("/home/test/ApplicationWrongMethod"));
	}
	
	@Test(expected = FileDoesNotExistException.class)
	public void ExecuteApplicationWithInvalidClass() {
		 ExecuteFileService service = new ExecuteFileService(userToken, "/home/test/dir/Application", args);
		 service.execute();
		
		 assertNotNull("application was not found", mydrive.getFileByAbsolutePath("/home/test/dir/Application"));
	}

	// More tests will be added here

}

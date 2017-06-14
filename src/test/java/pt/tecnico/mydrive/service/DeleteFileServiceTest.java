package pt.tecnico.mydrive.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Application;
import pt.tecnico.mydrive.domain.Link;

import pt.tecnico.mydrive.exception.permission.DeletePermissionException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

public class DeleteFileServiceTest extends AbstractServiceTest {

	private MyDriveManager mydrive;
	private User testUser;
	private Session userSession;
	private long userToken;
	private User rootUser;
	private Session rootSession;
	private long rootToken;

	@Override
	protected void populate() {
		mydrive = MyDriveManager.getInstance();
		testUser = new User(mydrive, "test", "Test User", "t35t....", "rwxd----");
		userSession = new Session(mydrive, "test", "t35t....");
		userToken = userSession.getToken();
		
		rootUser = mydrive.getUserByUsername("root");
		rootSession = new Session(mydrive, "root", "***");
		rootToken = rootSession.getToken();
		
		new Application(mydrive, testUser, userSession.getCurrentDirectory(), "hello world", "content");
		assertNotNull("hello app not created", mydrive.getFileByAbsolutePath("/home/test/hello world"));
		assertEquals("app creation fail", 3, mydrive.getFileByAbsolutePath("/home/test").getSize());
		
		new PlainFile(mydrive, testUser, userSession.getCurrentDirectory(), "plainboringfile");
		assertNotNull("plainfile not created", mydrive.getFileByAbsolutePath("/home/test/plainboringfile"));
		assertEquals("plainfile creation fail", 4, mydrive.getFileByAbsolutePath("/home/test").getSize());
		
		new Directory(mydrive, testUser, userSession.getCurrentDirectory(), "emptydir");
		assertNotNull("directory not created", mydrive.getFileByAbsolutePath("/home/test/emptydir"));
		assertEquals("directory creation fail", 5, mydrive.getFileByAbsolutePath("/home/test").getSize());
		
		Directory dir1 = new Directory(mydrive, testUser, userSession.getCurrentDirectory(), "justadir");
		assertNotNull("directory not created", mydrive.getFileByAbsolutePath("/home/test/justadir"));
		assertEquals("directory creation fail", 6, mydrive.getFileByAbsolutePath("/home/test").getSize());

		new Application(mydrive, testUser, dir1, "exterminate");
		assertNotNull("app not created", mydrive.getFileByAbsolutePath("/home/test/justadir/exterminate"));
		assertEquals("app creation fail", 3, dir1.getSize());

		Directory dir2 = new Directory(mydrive, testUser, dir1, "otherdir");
		assertNotNull("directory not created",
			mydrive.getFileByAbsolutePath("/home/test/justadir/otherdir"));
		assertEquals("directory creation fail", 4, dir1.getSize());

		new Link(mydrive, testUser, dir2, "linkedin", "/home/test");
		assertNotNull("link not created",
			mydrive.getFileByAbsolutePath("/home/test/justadir/otherdir/linkedin"));
		assertEquals("link creation fail", 3, dir2.getSize());

		new Link(mydrive, testUser, userSession.getCurrentDirectory(), "zelda",
			"/home/test/justadir/otherdir/linkedin");
		assertNotNull("link not created", mydrive.getFileByAbsolutePath("/home/test/zelda"));
		assertEquals("link creation fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
		
		new PlainFile(mydrive, rootUser, userSession.getCurrentDirectory(), "rootFile", "donotdelete");
		assertNotNull("plainfile not created", mydrive.getFileByAbsolutePath("/home/test/rootFile"));
		assertEquals("plainfile creation fail", 8, mydrive.getFileByAbsolutePath("/home/test").getSize());
		
	}
	
	
//success

	@Test
	public void deletePlainFileAsRootSuccess() {
		rootSession.changeDirectory(mydrive, "/home/test");
		DeleteFileService service = new DeleteFileService(rootToken, "plainboringfile");
		service.execute();
		
		assertNull("plainfile was not removed", mydrive.getFileByAbsolutePath("/home/test/plainboringfile"));
		assertEquals("plainfile removal fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
	}
	
	@Test
	public void deletePlainFileSuccess() {
		DeleteFileService service = new DeleteFileService(userToken, "plainboringfile");
		service.execute();
		
		assertNull("plainfile was not removed", mydrive.getFileByAbsolutePath("/home/test/plainboringfile"));
		assertEquals("plainfile removal fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
	}

	@Test
	public void deleteApplicationSuccess() {
		DeleteFileService service = new DeleteFileService(userToken, "hello world");
		service.execute();
		
		assertNull("app was not removed", mydrive.getFileByAbsolutePath("/home/test/hello world"));
		assertEquals("app removal fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
	}

	@Test
	public void deleteLinkSuccess() {
		DeleteFileService service = new DeleteFileService(userToken, "zelda");
		service.execute();
		
		assertNull("link was not removed", mydrive.getFileByAbsolutePath("/home/test/zelda"));
		assertEquals("link removal fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
	}
	
	@Test
	public void deleteEmptyDirectorySuccess() {
		DeleteFileService service = new DeleteFileService(userToken, "emptydir");
		service.execute();
		
		assertNull("directory was not removed", mydrive.getFileByAbsolutePath("/home/test/emptydir"));
		assertEquals("directory removal fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
	}

	@Test
	public void deleteDirectoryWithOwnedFilesSuccess() {
		DeleteFileService service = new DeleteFileService(userToken, "justadir");
		service.execute();
		
		assertNull("app was not removed", mydrive.getFileByAbsolutePath("/home/test/justadir/exterminate"));
		assertNull("directory was not removed", mydrive.getFileByAbsolutePath("/home/test/justadir/otherdir"));
		assertNull("directory was not removed", mydrive.getFileByAbsolutePath("/home/test/justadir"));
		assertEquals("directory removal fail", 7, mydrive.getFileByAbsolutePath("/home/test").getSize());
	}

	@Test
	public void checkServiceArgs(){
		DeleteFileService service = new DeleteFileService(userToken, "justadir");
		assertEquals("Directory has incorrect name", "justadir", service.getName());
		assertEquals("Service has incorrect token", userToken, service.getToken());
	}


//fail

	@Test(expected = FileDoesNotExistException.class)
	public void deleteNonExistingFile() {
		assertNull("rigged test, file exists", mydrive.getFileByAbsolutePath("/home/test/schrodingerFILE"));

		DeleteFileService service = new DeleteFileService(userToken, "schrodingerFILE");
		service.execute();
	}


	@Test(expected = DeletePermissionException.class)
	public void deleteOtherUserFileDeletablePermission() {
		DeleteFileService service = new DeleteFileService(userToken, "rootFile");
		service.execute();
	}


	@Test(expected = DeletePermissionException.class)
	public void deleteOtherUserFileNotDeletablePermission() {
		mydrive.getFileByAbsolutePath("/home/test/rootFile").setPermissions("rwx-----");

		DeleteFileService service = new DeleteFileService(userToken, "rootFile");
		service.execute();
	}


	@Test(expected = DeletePermissionException.class)
	public void deleteOwnedFileNotDeletablePermission() {
		mydrive.getFileByAbsolutePath("/home/test/plainboringfile").setPermissions("rwx-----");

		DeleteFileService service = new DeleteFileService(userToken, "plainboringfile");
		service.execute();
	}

	@Test(expected = InvalidTokenException.class)
    public void invalidToken(){
        long token = 189671;

        DeleteFileService service = new DeleteFileService(token, "rootFile");
        service.execute();
    }
}
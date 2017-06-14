package pt.tecnico.mydrive.service;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;
import pt.tecnico.mydrive.exception.file.creation.DuplicateFileException;
import pt.tecnico.mydrive.exception.file.creation.EmptyLinkException;
import pt.tecnico.mydrive.exception.file.creation.InvalidFileNameException;
import pt.tecnico.mydrive.exception.file.creation.PathTooLongException;
import pt.tecnico.mydrive.exception.permission.WritePermissionException;

public class CreateFileServiceTest extends AbstractServiceTest {

	private MyDriveManager mydrive;
	private Session session;
	private Session otherSession;
	private long token;
	private long otherToken;
	private long invalidToken;
	private String nameTooLong;

	@Override
	protected void populate() {
		mydrive = MyDriveManager.getInstance();
		new User(mydrive, "test", "Test User", "t35t....", "rwxd----");
		new User(mydrive, "otherTest", "Test User2", "t35t....", "xxxx----");
		session = new Session(mydrive, "test", "t35t....");
		otherSession = new Session(mydrive, "otherTest", "t35t....");
		token = session.getToken();
		otherToken = otherSession.getToken();
		invalidToken = -3212680428774279330L;
		nameTooLong = stringOfSize(1025, "a");
	}

	public String stringOfSize(int size, String string) {
		StringBuilder s = new StringBuilder();
		while (size-- > 0)
			s.append(string);

		return s.toString();
	}

	// Tests with Success

	@Test
	public void createDirectorySuccess() {
		CreateFileService service = new CreateFileService(token, FileType.DIRECTORY, "dir");
		service.execute();

		assertNotNull("directory was not created", mydrive.getFileByAbsolutePath("/home/test/dir"));
	}

	@Test
	public void createPlainFileWithoutContentSuccess() {
		CreateFileService service = new CreateFileService(token, FileType.PLAINFILE, "plain");
		service.execute();

		assertNotNull("plainfile was not created", mydrive.getFileByAbsolutePath("/home/test/plain"));
	}

	@Test
	public void createPlainFileWithContentSuccess() {
		CreateFileService service = new CreateFileService(token, FileType.PLAINFILE, "plain2", "hello plainfile");
		service.execute();

		assertNotNull("plainfile was not created", mydrive.getFileByAbsolutePath("/home/test/plain2"));
	}

	@Test
	public void createApplicationWithoutContentSuccess() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "app");
		service.execute();

		assertNotNull("application was not created", mydrive.getFileByAbsolutePath("/home/test/app"));
	}

	@Test
	public void createApplicationWithContentSuccess() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "app2", "hello application");
		service.execute();

		assertNotNull("application was not created", mydrive.getFileByAbsolutePath("/home/test/app2"));
	}

	@Test
	public void createLinkSuccess() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "app");
		service.execute();
		CreateFileService service2 = new CreateFileService(token, FileType.LINK, "url", "/home/test/app");
		service2.execute();

		assertNotNull("link was not created", mydrive.getFileByAbsolutePath("/home/test/url"));
	}

	@Test
	public void createDirectoryWithContentIgnoredSucess() {
		CreateFileService service = new CreateFileService(token, FileType.DIRECTORY, "dir", "anything");
		service.execute();

		assertNotNull("directory was not created", mydrive.getFileByAbsolutePath("/home/test/dir"));
	}

	@Test
	public void createLinkWithRelativePath() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "app");
		service.execute();
		CreateFileService service2 = new CreateFileService(token, FileType.LINK, "url", "app");
		service2.execute();

		assertNotNull("link was not created", mydrive.getFileByAbsolutePath("/home/test/url"));
	}

	@Test
	public void createLinksWithLoops() {
		CreateFileService service = new CreateFileService(token, FileType.LINK, "url", "/home/test/url2");
		service.execute();
		CreateFileService service2 = new CreateFileService(token, FileType.LINK, "url2", "/home/test/url");
		service2.execute();

		assertNotNull("link was not created", mydrive.getFileByAbsolutePath("/home/test/url"));
		assertNotNull("link was not created", mydrive.getFileByAbsolutePath("/home/test/url2"));
	}

	@Test // This test should work when the Application is a fully qualified name
	public void createApplicationMethodFullyQualifiedName() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "App",
				"home.test.Application.execute()");

		service.execute();

		assertNotNull("application was not created", mydrive.getFileByAbsolutePath("/home/test/App"));
	}

	@Test // This test should work when the Application is a fully qualified name
	public void createApplicationMainFullyQualifiedName() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "App", "home.test.Application");

		service.execute();

		assertNotNull("application was not created", mydrive.getFileByAbsolutePath("/home/test/App"));
	}

	// Tests without Success

	@Test(expected = InvalidTokenException.class)
	public void CreateFileWithInvalidToken() {
		CreateFileService service = new CreateFileService(invalidToken, FileType.DIRECTORY, "dir");
		service.execute();
	}

	@Test(expected = EmptyLinkException.class)
	public void createLinkWithEmptyContent() {
		CreateFileService service = new CreateFileService(token, FileType.LINK, "url", "");
		service.execute();
	}

	@Test(expected = DuplicateFileException.class)
	public void CreateFileAlreadyExisted() {
		CreateFileService service = new CreateFileService(token, FileType.PLAINFILE, "plain");
		service.execute();
		CreateFileService service2 = new CreateFileService(token, FileType.PLAINFILE, "plain");
		service2.execute();
	}

	@Test(expected = PathTooLongException.class)
	public void CreateFileWithNameTooLong() {
		CreateFileService service = new CreateFileService(token, FileType.PLAINFILE, nameTooLong);
		service.execute();
	}

	@Test(expected = InvalidFileNameException.class)
	public void createEmptyFile() {
		CreateFileService service = new CreateFileService(token, FileType.DIRECTORY, "");
		service.execute();
	}

	@Test(expected = InvalidFileNameException.class)
	public void createFileWithInvalidCharacter() {
		CreateFileService service = new CreateFileService(token, FileType.APPLICATION, "invalid/character");
		service.execute();
	}

	@Test(expected = WritePermissionException.class)
	public void createFileWithoutWritePermissions() {
		CreateFileService service = new CreateFileService(otherToken, FileType.DIRECTORY, "dir");
		service.execute();
	}

}

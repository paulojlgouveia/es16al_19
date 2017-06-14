package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import mockit.Mocked;
import mockit.MockUp;
import mockit.Expectations;
import mockit.integration.junit4.JMockit;

import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.file.access.NotReadableException;
import pt.tecnico.mydrive.exception.file.access.NotEditableException;
import pt.tecnico.mydrive.exception.permission.ReadPermissionException;;
import pt.tecnico.mydrive.exception.permission.WritePermissionException;;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

// permissions, file existance and other exceptions are already tested in the actual ReadFileService test
// this test makes no sense, we are asked to mock what we are supposed to be testing
// plus, it would be simpler to just ""realizar a semântica dos environment links no projeto""
	
@RunWith(JMockit.class)
public class EnvironmentLinkTest extends AbstractServiceTest {
	private MyDriveManager _mydrive;
	
	private User _rootUser;
	private Session _rootSession;
	private long _rootToken;
	private Directory _rootHdir;
	
	private User _testUser;
	private Session _userSession;
	private long _userToken;
	private Directory _userHdir;

	
	private PlainFile _plain1;
	private PlainFile _plain2;
	private Link _link1;
	private Link _link2;

	protected void populate(){
		_mydrive = MyDriveManager.getInstance();
		
		_rootUser = _mydrive.getUserByUsername("root");
		_rootSession = new Session(_mydrive, "root", "***");
		_rootToken = _rootSession.getToken();
		_rootHdir = _rootSession.getCurrentDirectory();
		
		_testUser = new User(_mydrive, "test", "Test User", "t35t_t35t", "rwxd----");
		_userSession = new Session(_mydrive, "test", "t35t_t35t");
		_userToken = _userSession.getToken();
		_userHdir = _userSession.getCurrentDirectory();
		
		new Link(_mydrive, _testUser, _userHdir, "dirlink", "$HOME/root/outerdir");
		new Link(_mydrive, _testUser, _userHdir, "rootfilelink", "$ROOTHOME/outerdir/plain1");

		
		Directory dir1 = new Directory(_mydrive, _rootUser, _rootHdir, "outerdir");
		Directory dir2 = new Directory(_mydrive, _rootUser, dir1, "middledir");
		Directory dir3 = new Directory(_mydrive, _rootUser, dir2, "innerdir");
		_plain1 = new PlainFile(_mydrive, _rootUser, dir1, "plain1", "file 1");
		_plain2 = new PlainFile(_mydrive, _rootUser, dir3, "plain2", "file 2");
		
		new Link(_mydrive, _rootUser, _rootHdir, "normallink", "/home/root/outerdir/plain1");
		new Link(_mydrive, _rootUser, _rootHdir, "innerlink", "$INNER/plain2");
	}
	

// mocked services //

	// mock aux function for the translation of environment variables
	public File translateHardwiredPath(String path) {
// 		System.out.println("\n\n" + _plain2.getAbsolutePath() + "\n\n");
		String realPath;
		
		switch(path) {
			case "$HOME/root/outerdir" :
				realPath = "/home/root/outerdir";
				break;
				
			case "$ROOTHOME/outerdir/plain1" :
				realPath = "/home/root/outerdir/plain1";
				break;
				
			case "$INNER/plain2" :
				realPath = "/home/root/outerdir/middledir/innerdir/plain2";
				break;	
				
			default :
				realPath = null;
				break;
		}
		return _mydrive.getFileByAbsolutePath(realPath);
	}
	
	public class MockedReadFileService extends MockUp<ReadFileService> {
		private final long _token;
		private final String _name;
		private File _file;
		
		public MockedReadFileService(long token, String name) {
			_token = token;
			_name = name;
		}
		
		@Mock
		public final void dispatch() {
			Session session = _mydrive.getSessionByToken(_token);
			
			if(session == null) {
				throw new InvalidTokenException(_token);
				
			} else {
				session.renew();
				
				String path = session.getCurrentDirectory().getEntry(_name).getContent();
				_file = translateHardwiredPath(path);
				
				if(_file == null)
					throw new FileDoesNotExistException(_name);
				
				User user = session.getUser();
				if(!user.hasReadPermission(_file))
					throw new ReadPermissionException(user.getUsername(), _file.getAbsolutePath());
			}
		}
		
		@Mock
		public final String result() {
			return _file.readContent();
		}
	};
	
	public class MockedWriteFileService extends MockUp<WriteFileService> {
		private final long _token;
		private final String _name;
		private final String _content;
		private File _file;
		
		public MockedWriteFileService(long token, String name, String content) {
			_token = token;
			_name = name;
			_content = content;
		}
		
		@Mock
		public final void dispatch() {
			Session session = _mydrive.getSessionByToken(_token);
			
			if(session == null) {
				throw new InvalidTokenException(_token);
				
			} else {
				session.renew();
				
				String path = session.getCurrentDirectory().getEntry(_name).getContent();
				_file = translateHardwiredPath(path);
				
				if(_file == null)
					throw new FileDoesNotExistException(_name);
				
				User user = session.getUser();
				if(!user.hasWritePermission(_file))
					throw new WritePermissionException(user.getUsername(), _file.getAbsolutePath());
					
				_file.setContent(_content);
			}
		}
	};
	
	
// ReadFileService tests //

	@Test
	public void notMockedReadRegularLink() {
		
		ReadFileService rfs = new ReadFileService(_rootToken,"normallink");
		rfs.execute();
		assertEquals("file 1", rfs.result());
	}
	
	@Test
	public void mockedReadLinkToInnerFile() {
	
		new MockedReadFileService(_rootToken, "innerlink");
    	
		ReadFileService rfs = new ReadFileService(_rootToken,"innerlink");
		rfs.execute();
		assertEquals("file 2", rfs.result());
	}
	
	@Test(expected = NotReadableException.class)
	public void mockedReadLinkToDirectory() {
		
		_rootSession.changeDirectory(_mydrive, "/home/test");
		
		new MockedReadFileService(_rootToken, "dirlink");
		
		ReadFileService rfs = new ReadFileService(_rootToken,"dirlink");
		rfs.execute();
		assertNotNull("directories should not be readable",rfs.result());
	}
	
	@Test(expected = ReadPermissionException.class)
	public void mockedReadLinkToOtherUserFile() {
		
		new MockedReadFileService(_userToken, "rootfilelink");
		
		ReadFileService rfs = new ReadFileService(_userToken,"rootfilelink");
		rfs.execute();
		assertNotNull("should have thrown ReadPermissionException",rfs.result());
	}
	
	
	
// WriteFileService tests //

	@Test
	public void notMockedWriteRegularLink() {
		
		WriteFileService wfs = new WriteFileService(_rootToken,"normallink", "new content");
		wfs.execute();
		assertEquals("new content", _plain1.readContent());
	}
	
	@Test
	public void mockedWriteLinkToInnerFile() {
		
		new MockedWriteFileService(_rootToken, "innerlink", "new content");
    	
		WriteFileService wfs = new WriteFileService(_rootToken,"innerlink", "new content");
		wfs.execute();
		assertEquals("new content", _plain2.readContent());
	}
	
	@Test(expected = NotEditableException.class)
	public void mockedWriteLinkToDirectory() {
		
		_rootSession.changeDirectory(_mydrive, "/home/test");
		
		new MockedWriteFileService(_rootToken, "dirlink", "some content");
		
		WriteFileService wfs = new WriteFileService(_rootToken,"dirlink", "some content");
		wfs.execute();
	}
	
	@Test(expected = WritePermissionException.class)
	public void mockedWriteLinkToOtherUserFile() {
		
		new MockedWriteFileService(_userToken, "rootfilelink", "some content");
		
		WriteFileService wfs = new WriteFileService(_userToken,"rootfilelink", "some content");
		wfs.execute();
	}
	
	
}
	


package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.session.InvalidTokenException;
import pt.tecnico.mydrive.exception.file.access.NotDirectoryException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.permission.ExecutePermissionException;

public class ChangeDirectoryServiceTest extends AbstractServiceTest{

	private MyDriveManager mydrive;
	private ChangeDirectoryService cds;
	private Session s;
	private long sessionToken,stok2;
	private Directory rootHome;
	private User root;
	
	protected void populate(){
		mydrive = MyDriveManager.getInstance();
		
		root = mydrive.getUserByUsername("root");
		User darklord = new User(mydrive,"BloodyDarkLord666","Filomeno Meireles","kappa123","rw-d----");
		s = new Session(mydrive,"BloodyDarkLord666","kappa123");
		stok2 = s.getToken();
		Directory dird = new Directory(mydrive,darklord,darklord.getHomeDirectory(),"dird");
		rootHome = root.getHomeDirectory();
		Directory dira = new Directory(mydrive,root,rootHome,"dira");
		Link linkx = new Link(mydrive,root,rootHome,"link","/home/root");
		Directory dirb = new Directory(mydrive,root,rootHome.getParentDirectory(),"dirb");
		Directory dirc = new Directory(mydrive,root,dira,"dirc");
		s = new Session(mydrive,"root","***");
		sessionToken = s.getToken();
			
	}
	
	@Test
	public void rootCanSearchWithoutExecutePerm(){
		s.changeDirectory(mydrive,"/home/BloodyDarkLord666");
		cds = new ChangeDirectoryService(sessionToken,"dird");
		cds.execute();
		assertEquals("Root user can search even without execut permission","/home/BloodyDarkLord666/dird"
				                                                          ,cds.result());
		
	}
	
	@Test(expected = ExecutePermissionException.class)
	public void cannotSearchWithoutExecutePermAbsolute(){
		s.changeDirectory(mydrive,"/home/BloodyDarkLord666/");
		cds = new ChangeDirectoryService(stok2,"/home/BloodyDarkLord666/dird");
		cds.execute();
	}
	
	@Test(expected = ExecutePermissionException.class)
	public void cannotSearchWithoutExecutePermRelative() {
		s.changeDirectory(mydrive,"/home/BloodyDarkLord666");
		cds = new ChangeDirectoryService(stok2,"dird");
		cds.execute();
	}
	
	@Test
	public void absoluteToDirectlyConnected(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"/home/root/dira");
		cds.execute();
		assertEquals("Absolute path to a directly connected dir.","/home/root/dira",cds.result());
	}
	
	@Test
	public void relativeToDirectlyConnected(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"dira");
		cds.execute();
		assertEquals("Relative path to a directly connected dir.","/home/root/dira",cds.result());
	}
	
	@Test
	public void dotWorks(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,".");
		cds.execute();
		assertEquals("'.' shows current directory.","/home/root",cds.result());
	}
	
	@Test
	public void relativeAbsoluteToSameDir(){
		String absolute,relative;
		
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"dira");
		cds.execute();
		relative = cds.result();
		
		cds = new ChangeDirectoryService(sessionToken,"/home/root/dira");
		cds.execute();
		absolute = cds.result();
		
		assertEquals("Relative and absolute paths to the same directory take us to the same directory",
				     relative,absolute);
	}

	@Test
	public void absoluteToNotDirectlyConnected(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"/home/dirb");
		cds.execute();
		assertEquals("Absolute path to a non-directly connected dir.","/home/dirb",cds.result());
	}

	@Test
	public void relativeToNotDirectlyConnected(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"dira/dirc");
		cds.execute();
		assertEquals("Relative path to a non-directly connected dir.","/home/root/dira/dirc",cds.result());
	}
	
	@Test
	public void dotDotWorks(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"..");
		cds.execute();
		assertEquals("'..' changes to parent directory.","/home",cds.result());
	}
	
	@Test
	public void dotDotRoot(){
		s.changeDirectory(mydrive,"/");
		cds = new ChangeDirectoryService(sessionToken,"..");
		cds.execute();
		assertEquals("'..' in the root directory stays in the root directory.","/",cds.result());
	}
	
	@Test
	public void absoluteToCurrentDir(){
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"/home/root");
		cds.execute();
		assertEquals("Absolute path to the current directory stays in the current directory.",
				     "/home/root",cds.result());
	}

	@Test(expected = FileDoesNotExistException.class)
    public void changeToInvalidDirectory() {
		s.changeDirectory(mydrive,"/home/root");
		cds = new ChangeDirectoryService(sessionToken,"/home/root/howcanmirrorsberealifoureyesarentreal");
		cds.execute();
    }

	// @Test(expected = NotDirectoryException.class)
	// 	public void changeToNotDirectory(){
	// 	s.changeDirectory(mydrive,"/home/root");
	// 	cds = new ChangeDirectoryService(sessionToken,"/home/root/link");
	// 	cds.execute();
	// }

	@Test(expected = InvalidTokenException.class)
	public void invalidToken(){
		cds = new ChangeDirectoryService(12345678,"/home/root/link");
		cds.execute();
	}
}
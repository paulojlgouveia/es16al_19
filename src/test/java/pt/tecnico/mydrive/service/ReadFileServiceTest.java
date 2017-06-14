
package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.file.access.NotReadableException;
import pt.tecnico.mydrive.exception.permission.ReadPermissionException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

public class ReadFileServiceTest extends AbstractServiceTest{
	
	private long sessionAToken,sessionBToken,sessionCToken;
	private MyDriveManager mydrive;
	private ReadFileService rf;
	
	protected void populate(){
		mydrive = MyDriveManager.getInstance();
		
		User root = mydrive.getUserByUsername("root");
		Directory homea = root.getHomeDirectory();
		PlainFile plainfilea = new PlainFile(mydrive,root,homea,"plainfilea","Good Morning.");
		Link linka = new Link(mydrive,root,homea,"linka",plainfilea.getAbsolutePath());
		Link linka2 = new Link(mydrive,root,homea,"linka2",linka.getAbsolutePath());
		Application appa = new Application(mydrive,root,homea,"appa","oddfuture.wolfgang.killthemall");
		Directory dira = new Directory(mydrive,root,homea,"dira");
		Session sa = new Session(mydrive,"root","***");
		sessionAToken = sa.getToken();
		
		User darklord = new User(mydrive,"BloodyDarkLord666","Filomeno Meireles","kappa123","rwxd--x-");
		Directory homeb = darklord.getHomeDirectory();
		PlainFile plainfileb = new PlainFile(mydrive,darklord,homeb,"plainfileb","Wake me up \nWake me up inside"
																	        + "\nSave me");
		Session sb = new Session(mydrive,"BloodyDarkLord666","kappa123");
		sessionBToken = sb.getToken();
		
		User weedwizard = new User(mydrive,"xWeedWizardx","Tiago Eusebio","phandir","-wxdr-x-");
		Directory homec = weedwizard.getHomeDirectory();
		PlainFile plainfilec = new PlainFile(mydrive,weedwizard,homec,"plainfilec");
		Session sc = new Session(mydrive,"xWeedWizardx","phandir");
		sessionCToken = sc.getToken();
	}

	@Test
	public void userReadsOwnPlainFileSuccess(){
		rf = new ReadFileService(sessionAToken,"plainfilea");
		rf.execute();
		assertEquals("A user can read his own PlainText file","Good Morning.",rf.result());
	}
	
	@Test
	public void applicationReadSuccess(){
		rf = new ReadFileService(sessionAToken,"appa");
		rf.execute();
		assertEquals("Application file is read correctly","oddfuture.wolfgang.killthemall",rf.result());
	}
	
	@Test
	public void linkReadSuccess(){
		rf = new ReadFileService(sessionAToken,"linka");
		rf.execute();
		assertEquals("Link file is read correctly","Good Morning.",rf.result());
	}
	
	@Test
	public void linkToLinkReadSuccess(){
		rf = new ReadFileService(sessionAToken,"linka2");
		rf.execute();
		assertEquals("Link to link is read correctly","Good Morning.",rf.result());
	}
	
	@Test
	public void rootReadsAnyFileSuccess(){
		User root = mydrive.getUserByUsername("root");
		Session aux = mydrive.getSessionByToken(sessionAToken);
		aux.changeDirectory(mydrive,"/home/BloodyDarkLord666");
		rf = new ReadFileService(sessionAToken,"plainfileb");
		rf.execute();
		aux.changeDirectory(mydrive,"/home/root");
		assertEquals("Root can read any file even without second r permission","Wake me up \nWake me up inside"
																				+ "\nSave me",rf.result());
	}

	@Test(expected = ReadPermissionException.class)
    public void unauthorizedOtherFileRead() {
		User weedwizard = mydrive.getUserByUsername("xWeedWizardx");
		Session aux = mydrive.getSessionByToken(sessionCToken);
		aux.changeDirectory(mydrive,"/home/BloodyDarkLord666");
		ReadFileService rf = new ReadFileService(sessionCToken,"plainfileb");
		rf.execute();
    }
	
	@Test(expected = ReadPermissionException.class)
    public void unauthorizedOwnFileRead() {
		User weedwizard = mydrive.getUserByUsername("xWeedWizardx");
		Session aux = mydrive.getSessionByToken(sessionCToken);
		aux.changeDirectory(mydrive,"/home/xWeedWizardx");
		ReadFileService rf = new ReadFileService(sessionCToken,"plainfilec");
		rf.execute();
    }

	@Test(expected = NotReadableException.class)
    public void directoryNotReadable() {
		User root = mydrive.getUserByUsername("root");
		Session aux = mydrive.getSessionByToken(sessionAToken);
		aux.changeDirectory(mydrive,"/home/root");
		ReadFileService rf = new ReadFileService(sessionAToken,"dira");
		rf.execute();
    }
	
	@Test(expected = FileDoesNotExistException.class)
    public void fileNotInCurrentDirectory() {
		User root = mydrive.getUserByUsername("root");
		Session aux = mydrive.getSessionByToken(sessionAToken);
		aux.changeDirectory(mydrive,"/home/root");
		ReadFileService rf = new ReadFileService(sessionAToken,"plainfilec");
		rf.execute();
    }
    
    @Test(expected = InvalidTokenException.class)
	public void invalidToken(){
		ReadFileService rfs = new ReadFileService(54675884,"plainfilea");
		rfs.execute();
	}
	
}

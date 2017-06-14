package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.Expectations;
import mockit.Verifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.Application;
import pt.tecnico.mydrive.domain.PlainFile;
import pt.tecnico.mydrive.domain.Link;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.Directory;

import pt.tecnico.mydrive.exception.permission.ExecutePermissionException;

@RunWith(JMockit.class)
public class ExecuteAssociationTest extends AbstractServiceTest{
	private static String[] a1,a3;
	private String[] a2 = {"a","b","c"}; 
	
	private User user1,user2;
	private Application app1,app2;
	private Link l1;
	private PlainFile pf1;
	private Directory d1,d2;
	private ExecuteFileService service;
	private MyDriveManager mydrive;
	private Session session1,session2;
	long token1,token2;
	
	public static void test1(String[] args){
		a1 = args;
	}
	public static void test2(String[] args){
		a3 = args;
	}
	
	protected void populate(){
		mydrive = MyDriveManager.getInstance();
		user1 = new User(mydrive,"bob123","bob","kappablabla89","rwxd--x-");
		user2 = new User(mydrive,"sike","soja","kappablabla89","rw-d--x-");
		session1 = new Session(mydrive,"bob123","kappablabla89");
		session2 = new Session(mydrive,"sike","kappablabla89");
		token1 = session1.getToken();
		token2 = session2.getToken();
		app1 = new Application(mydrive,user1,user1.getHomeDirectory(),"app1",
											"pt.tecnico.mydrive.service.ExecuteAssociationTest.test1()");
		app2 = new Application(mydrive,user2,user2.getHomeDirectory(),"app2",
											"pt.tecnico.mydrive.service.ExecuteAssociationTest.test2()");
		pf1 = new PlainFile(mydrive,user1,user1.getHomeDirectory(),"pf1","");
		l1 = new Link(mydrive,user2,user2.getHomeDirectory(),"l1",app2.getAbsolutePath());
		d1 = new Directory(mydrive,user1,user1.getHomeDirectory(),"d1");
		d2 = new Directory(mydrive,user2,user2.getHomeDirectory(),"d2");
		a1 = null;
		a3 = null;
	}
	
	@Test
	public void simpleExecution(){

		new MockUp<ExecuteFileService>() {
			@Mock
			public void dispatch(){ app1.execute(mydrive,a2); } ;
		};
		service = new ExecuteFileService(token1,pf1.getAbsolutePath(),a2);
		service.execute();
		assertEquals(a1.length,3);
		assertTrue(a1.equals(a2));
	}
	
	@Test
	public void linkExecution() {

		new MockUp<ExecuteFileService>() {
			@Mock
			public void dispatch(){ l1.execute(mydrive,a2);
									app1.execute(mydrive,a2);} ;
		};
		service = new ExecuteFileService(token2,pf1.getAbsolutePath(),a2);
		service.execute();
		assertEquals(a1.length,3);
		assertEquals(a2.length,3);
		assertTrue(a1.equals(a2));
		assertTrue(a3.equals(a2));
	}
	
	@Test
	public void directoryExecutionWithExtension(){
		new MockUp<ExecuteFileService>() {
			@Mock
			public void dispatch(){ app1.execute(mydrive,a2); } ;
		};
		service = new ExecuteFileService(token1,d1.getAbsolutePath(),a2);
		service.execute();
		assertEquals(a1.length,3);
		assertTrue(a1.equals(a2));
	}
	
	@Test
	public void directoryExecutionWithoutExtension(){
		new MockUp<ExecuteFileService>() {
			@Mock
			public void dispatch(){ } ;
		};
		service = new ExecuteFileService(token1,d2.getAbsolutePath(),a2);
		service.execute();
		assertNull(a1);
	}
	
	@Test(expected = ExecutePermissionException.class)
	public void executePermissionsTest(){
		new MockUp<ExecuteFileService>() {
			@Mock
			public void dispatch(){ throw new ExecutePermissionException("sike","l1"); } ;
		};
		service = new ExecuteFileService(token2,d1.getAbsolutePath(),a2);
		service.execute();
	}
	
	
}

package pt.tecnico.mydrive.service;

import org.junit.Test;

import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.user.*;
import pt.tecnico.mydrive.exception.login.*;

import pt.tecnico.mydrive.exception.user.UserDoesNotExistException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginUserServiceTest extends AbstractServiceTest {

	private final String USERNAME = "mario";
	private final String NAME = "Mario";
	private final String PASSWORD = "mushroom";
	private final String MASK = "rwxd----";
	
	private final String ROOT_USERNAME = "root";
	private final String ROOT_PASSWORD = "***";
	
    private final String INVALID_USERNAME = "luigi";
    private final String INVALID_PASSWORD = "turtle";

	private MyDriveManager mydrive;

    protected void populate() {
        mydrive = MyDriveManager.getInstance();
    }

	@Test(expected = UserDoesNotExistException.class)
	public void loginUserInvalidUser() throws Exception {
		new User(mydrive, USERNAME, NAME, PASSWORD, MASK);
		LoginUserService login = new LoginUserService(INVALID_USERNAME, PASSWORD);
		login.execute();
	}
	
	@Test(expected = WrongPasswordException.class)
	public void loginUserInvalidPassword() throws Exception {
		new User(mydrive, USERNAME, NAME, PASSWORD, MASK);
		LoginUserService login = new LoginUserService(USERNAME, INVALID_PASSWORD);
		login.execute();
	}
	
	@Test
	public void loginUserSucess() throws Exception {
		new User(mydrive, USERNAME, NAME, PASSWORD, MASK);
		LoginUserService login = new LoginUserService(USERNAME, PASSWORD);

		login.execute();
		assertEquals("Session was not created", 1, mydrive.getSessionSet().size());
		
		long token = login.result();
		assertNotNull("Returned token is null", token);
		
		User user = mydrive.getUserByToken(token);
		assertNotNull("Invalid token", user);
		assertEquals("Invalid session user", USERNAME, user.getUsername());
	}
	
	@Test
	public void loginUserRootSucess() throws Exception {
		LoginUserService login = new LoginUserService(ROOT_USERNAME, ROOT_PASSWORD);

		login.execute();
		assertEquals("Session was not created", 1, mydrive.getSessionSet().size());
		
		long token = login.result();
		assertNotNull("Returned token is null", token);
		
		User user = mydrive.getUserByToken(token);
		assertNotNull("Invalid token", user);
		assertEquals("Invalid session user", ROOT_USERNAME, user.getUsername());
	}
	
	@Test
	public void loginUserTwiceSucess() throws Exception {
		new User(mydrive, USERNAME, NAME, PASSWORD, MASK);
		LoginUserService login = new LoginUserService(USERNAME, PASSWORD);
		
		login.execute();
		assertEquals("Session was not created", 1, mydrive.getSessionSet().size());
		
		long token = login.result();
		assertEquals("Invalid session user", USERNAME, mydrive.getUserByToken(token).getUsername());
		
		login.execute();
		assertEquals("Second session was not created", 2, mydrive.getSessionSet().size());
		
		token = login.result();
		assertEquals("Invalid session user", USERNAME, mydrive.getUserByToken(token).getUsername());
	}

	@Test(expected = UserDoesNotExistException.class)
	public void login(){
		LoginUserService login = new LoginUserService("Sheldor", true);

		login.execute();
	}

	@Test
	public void loginUserNobody(){
		LoginUserService login = new LoginUserService("nobody");

		login.execute();
		long token = login.result();

		assertNotNull("Invalid token", token);
	}
}
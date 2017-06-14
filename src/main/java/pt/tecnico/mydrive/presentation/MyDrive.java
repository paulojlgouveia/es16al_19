package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.LoginUserService;
import pt.tecnico.mydrive.MyDriveApplication;

import java.io.File;

public class MyDrive extends Shell {

	private long token;
	private String username;
	private String variable;
	
	public MyDrive() { // add commands here
		super("MyDrive");
		new Login(this);
		new Key(this);
		new ChangeDirectory(this);
		new List(this);
		new Environment(this);
		new Execute(this);
		new Write(this);
		loginGuestUser();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setToken(long token) {
		this.token = token;
	}

	public long getToken() {
		return token;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getVariable() {
		return variable;
	}
	
	public void loginGuestUser() {
		LoginUserService loginNobody = new LoginUserService("nobody");
		loginNobody.execute();
		token = loginNobody.result();
		setUsername("nobody");
		setToken(token);
	}
	
	public static void main(String[] args) throws Exception {
		MyDrive mydrive = new MyDrive();
		MyDriveApplication mda = new MyDriveApplication();


		//mda.test();
			
		for (String s: args)
			mda.xmlScan(new File(s));
			
		mda.xmlPrint();

		mydrive.execute();
	}
}


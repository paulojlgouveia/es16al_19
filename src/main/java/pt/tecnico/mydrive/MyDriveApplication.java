package pt.tecnico.mydrive;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import pt.tecnico.mydrive.exception.xml.*;
import pt.tecnico.mydrive.exception.file.creation.DuplicateFileException;
import pt.tecnico.mydrive.exception.login.WrongPasswordException;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;
import pt.tecnico.mydrive.exception.user.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.user.creation.DuplicateUsernameException;
import pt.tecnico.mydrive.exception.user.creation.InvalidUsernameException;
import pt.tecnico.mydrive.domain.MyDriveManager;
import pt.tecnico.mydrive.domain.User;

import pt.tecnico.mydrive.service.*;

public class MyDriveApplication {
	static final Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args) throws IOException {
		System.out.println("*** Welcome to the MyDrive Application! ***");
		try{
			MyDriveManager mydrive = null;
			
			test();
			
			for (String s: args)
				xmlScan(new File(s));
			
			xmlPrint();

		} finally {
			FenixFramework.shutdown();
		}
	}

	@Atomic
	public static void test() {
		try {
		log.trace("Setup: " + FenixFramework.getDomainRoot());
		MyDriveManager mydrive = MyDriveManager.getInstance();
		
//consistency check
		System.out.println("\n ----- list root files ----- \n");
		mydrive.getUserByUsername("root").printFileSet();
		if(mydrive.getUserByUsername("test") != null) {	// if running for the first time user doesnt exist
			System.out.println("\n ----- list testuser files ----- \n");
			mydrive.getUserByUsername("test").printFileSet();
		} System.out.println();
		
//user creation
		System.out.println("\n ----- listUsers ----- \n");
		mydrive.listUsers();
		
		System.out.println(" ----- addUser test and test2 ----- \n");
		try{
			new User(mydrive, "test", "Test User", "t3st", "rwxd----");
			new User(mydrive, "test2", "Test User2", "t3st2", "rwxd----");
		} catch (DuplicateUsernameException e) { System.out.println(e.getMessage()); }

		System.out.println("\n ----- listUsers ----- \n");
		mydrive.listUsers();
		
		System.out.println(" ----- addUser @invalid ----- \n");
		try{
			new User(mydrive, "abc@", "Teste abc", "abc", "rwxd----");
		} catch (InvalidUsernameException e) { System.out.println(e.getMessage()); }
		
		System.out.println(" ----- addUser 2characters ----- \n");
		try{
			new User(mydrive, "ab", "Test ab", "ab", "rwxd----");
		} catch (InvalidUsernameException e) { System.out.println(e.getMessage()); }

		System.out.println(" ----- addUser alreadyExist ----- \n");
		try{
			new User(mydrive, "test", "Test User", "t3st", "rwxd----");
		} catch (DuplicateUsernameException e) { System.out.println(e.getMessage()); }
		catch (DuplicateFileException e) { System.out.println(e.getMessage()); }

        
// directory creation
        System.out.println(" ----- list / dir ----- \n");
		System.out.println(mydrive.getFileByAbsolutePath("/").list());
		
        System.out.println(" ----- list home dir ----- \n");
		System.out.println(mydrive.getFileByAbsolutePath("/home/").list());
        
		System.out.println(" ----- list home/test dir ----- \n");
		System.out.println(mydrive.getFileByAbsolutePath("/home/test").list());
        
        
// user removal
		System.out.println(" ----- removeUser test2 ----- \n");
        mydrive.getUserByUsername("test2").remove();
		System.out.println("\n ----- listUsers ----- \n");
		mydrive.listUsers();
        
        System.out.println(" ----- list home dir ----- \n");
		System.out.println(mydrive.getFileByAbsolutePath("/home/").list());
        
        
//login service
		System.out.println("\n ----- LoginUserService root ----- \n");
		LoginUserService login = new LoginUserService("root", "***");
		login.execute();
		
		System.out.println("\n ----- LoginUserService test ----- \n");
		LoginUserService loginTest = new LoginUserService("test", "t3st");
		loginTest.execute();
		
		System.out.println("\n ----- LoginUserService nobody ----- \n");
		LoginUserService loginNobody = new LoginUserService("nobody");
		loginNobody.execute();
		
		System.out.println("\n ----- LoginUserService doesNotExist ----- \n");
		try{
			LoginUserService login2 = new LoginUserService("abc", "xxx");
			login2.execute();
		} catch (UserDoesNotExistException e) { System.out.println(e.getMessage()); }
		
		System.out.println("\n ----- LoginUserService wrongPass ----- \n");
		try{
			LoginUserService login3 = new LoginUserService("test", "wrong");
			login3.execute();
		} catch (WrongPasswordException e) { System.out.println(e.getMessage()); }

		System.out.println("\n ----- listAllSessions ----- \n");
		mydrive.listSessions();
		
		
//consistency check
		System.out.println("\n ----- list root files ----- \n");
		mydrive.getUserByUsername("root").printFileSet();
		System.out.println("\n ----- list testuser files ----- \n");
		mydrive.getUserByUsername("test").printFileSet(); System.out.println();
		

// commented because enums
// //CreateFileService service -> tokens hardwired, they are diferent on your PC
// 		System.out.println("\n ----- CreateDir root ----- \n");
// 		try {
// 			CreateFileService CreateFileServiceservice = new CreateFileService(-3212680428774279330L, "directory", "roottest_DIR");
// 			CreateFileServiceservice.execute();
// 					
// 		System.out.println("\n ----- CreateText root ----- \n");
// 		CreateFileServiceservice = new CreateFileService(-3212680428774279330L, "plainfile", "roottest_TXT", "hello");
// 		CreateFileServiceservice.execute();
// 		
// 		System.out.println("\n ----- CreateDir user ----- \n");
// 		CreateFileServiceservice = new CreateFileService(-4238481205119614736L, "directory", "test_DIR");
// 		CreateFileServiceservice.execute();
// 		
// 		System.out.println("\n ----- CreateText user ----- \n");
// 		CreateFileServiceservice = new CreateFileService(-4238481205119614736L, "plainfile", "test_TXT", "world");
// 		CreateFileServiceservice.execute();
// 		} catch (InvalidTokenException e) { System.out.println(e.getMessage()); }
// 		
//consistency check
		System.out.println("\n ----- list root files -----");
		mydrive.getUserByUsername("root").printFileSet();
		System.out.println("\n ----- list testuser files -----");
		mydrive.getUserByUsername("test").printFileSet(); System.out.println();
		
//remove file check
		System.out.println("\n ----- remove test_DIR -----");
		try{
			mydrive.getFileByAbsolutePath("/home/test/test_DIR").remove();
		} catch (Exception e) { System.out.println("Removefile FAIL -> " + e.getMessage()); }

		
//consistency check
		System.out.println("\n ----- list root files -----");
		mydrive.getUserByUsername("root").printFileSet();
		System.out.println("\n ----- list testuser files -----");
		mydrive.getUserByUsername("test").printFileSet(); System.out.println();
		
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	

    @Atomic
    public static void init() { // empty MyDriveManager
        log.trace("Init: " + FenixFramework.getDomainRoot());
        MyDriveManager.getInstance().cleanup();
    }
    
	
	@Atomic
	public static void xmlPrint() {
		log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
		try {
			Document doc = MyDriveManager.getInstance().xmlExport();
			XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());

			xmlOutput.output(doc, new PrintStream("mydriveExport.xml"));
			xmlOutput.output(doc, new PrintStream(System.out));
		} catch (IOException | ExportException e) {
			System.out.println(e);
		}
	}
	
	@Atomic
	public static void xmlScan(File file) {
		log.trace(">>> xmlScan: " + FenixFramework.getDomainRoot() + " <<<");
		MyDriveManager md = MyDriveManager.getInstance();
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document)builder.build(file);
			md.xmlImport(document.getRootElement());
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}
}






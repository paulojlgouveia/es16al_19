package pt.tecnico.mydrive.integration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.service.AbstractServiceTest;
import pt.tecnico.mydrive.presentation.*;
import pt.tecnico.mydrive.domain.*;

import pt.tecnico.mydrive.exception.user.UserDoesNotExistException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;
import pt.tecnico.mydrive.exception.permission.ExecutePermissionException;

/*
Tests:
    Login
    Change Working Directory
    List
    Execute
    Write
    Environment
    Key
*/


public class SystemTest extends AbstractServiceTest {

    private MyDrive mds;
    private User u1, u2;
    private Directory d;
    private Link l;
    private Application app1, app2;
    private PlainFile pf1, pf2;

    protected void populate() {
        mds = new MyDrive();

        MyDriveManager md = MyDriveManager.getInstance();

        // User 1
        u1 = new User(md, "sheldor", "Sheldon Cooper", "bazinga");
        d = new Directory(md, u1, u1.getHomeDirectory(), "phd");
        l = new Link(md, u1, d, "HomeLink", "..");
        app1 = new Application(md, u1, d, "Thesis","pt.tecnico.mydrive.integration.SystemTest.testingDo()");
        app2 = new Application(md, u1, d, "SoftKitty","pt.tecnico.mydrive.integration.SystemTest.testingDo1()");
        pf1 = new PlainFile(md, u1, d, "List of stuff I hate", "1-Human Contact\n2-Number 1");
        pf2 = new PlainFile(md, u1, d, "Baaamm!", "" + app2.getAbsolutePath() + " " + "arg1\n" + app2.getAbsolutePath() + " " + "arg1 arg2\n");
        pf2.setExtension("baz");
        //a = new Association("baz",u1,app1);

        // User 2
        u2 = new User(md, "Wolowizard", "Howard Wolowitz", "iAmCrazy");
    }

    @Test
    public void success(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new ChangeDirectory(mds).execute(new String[] {d.getAbsolutePath()});
        new List(mds).execute(new String[] {d.getAbsolutePath()});
        new Execute(mds).execute(new String[] {app1.getAbsolutePath(), "Lizard poisons", "Spock"});
        new Execute(mds).execute(new String[] {pf2.getAbsolutePath(), "banana", "split"});
        new Write(mds).execute(new String[] {pf1.getAbsolutePath(),"1-Pennys high pitch laughter"});
        new Environment(mds).execute(new String[] {"Green lantern", "lantern"});
        new Environment(mds).execute(new String[] {"Green lantern"});
        new Environment(mds).execute(new String[] {"Rat Man"});
        new Environment(mds).execute(new String[] {"Green lantern", "sucks"});
        new Key(mds).execute(new String[] {u2.getUsername()});
    }

    public static void testingDo(String[] args){
    }
    public static void testingDo1(String[] args){
    }

    @Test
    public void successLessArgs(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Execute(mds).execute(new String[] {app1.getAbsolutePath()});
        new Environment(mds).execute(new String[] {"Green lantern"});
    }

    @Test
    public void successNoArgs(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new ChangeDirectory(mds).execute(new String[] {});
        new List(mds).execute(new String[] {});
        new Environment(mds).execute(new String[] {"Green lantern", "lantern"});
        new Environment(mds).execute(new String[] {});
        new Key(mds).execute(new String[] {});
    }


// PRESENTATION LAYER TESTS (NAME OF COMMANDS AND LIMIT VALUES)

//      LOGIN TESTS
    @Test
    public void loginName(){
        Login l = new Login(mds);
        assertEquals("Login Command name isn't login", "login", l.name());
    }

    @Test(expected = RuntimeException.class)
    public void loginWithoutArgs() {
        new Login(mds).execute(new String[] {});
    }

    @Test(expected = RuntimeException.class)
    public void loginOver9000Args() {
        new Login(mds).execute(new String[] {"You", "Shall", "Not", "Pass!!!"});
    }

    @Test(expected = UserDoesNotExistException.class)
    public void loginInvalidUser1Arg(){
        new Login(mds).execute(new String[] {"Neo"});
    }

    @Test
    public void loginNobody(){
        new Login(mds).execute(new String[] {"nobody"});
    }

    @Test(expected = UserDoesNotExistException.class)
    public void loginInvalidUser2Arg(){
        new Login(mds).execute(new String[] {"AC", "DC"});
    }

    @Test
    public void loginRoot(){
        new Login(mds).execute(new String[] {"root", "***"});
    }


//      CHANGE WORKING DIRECTORY
    @Test
    public void changeDirName(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        ChangeDirectory cd = new ChangeDirectory(mds);
        assertEquals("ChangeDirectory Command name isn't cwd", "cwd", cd.name());
    }

    @Test(expected = RuntimeException.class)
    public void changeDirOver9000Args() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new ChangeDirectory(mds).execute(new String[] {"You", "Shall", "Not", "Pass!!!"});
    }

    @Test(expected = ExecutePermissionException.class)
    public void changeDirWithoutPermission(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new ChangeDirectory(mds).execute(new String[] {d.getAbsolutePath()});
    }

//     LIST
    @Test
    public void listName(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        List l = new List(mds);
        assertEquals("List Command name isn't ls", "ls", l.name());
    }

    @Test(expected = RuntimeException.class)
    public void listOver9000Args() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new List(mds).execute(new String[] {"You", "Shall", "Not", "Pass!!!"});
    }

    @Test(expected = ExecutePermissionException.class)
    public void listWithoutPermission(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new List(mds).execute(new String[] {d.getAbsolutePath()});
    }


//      EXECUTE
    @Test
    public void executeName(){
        Execute exe = new Execute(mds);
        assertEquals("Execute Command name isn't do", "do", exe.name());
    }

    @Test(expected = RuntimeException.class)
    public void executeWithoutArgs() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Execute(mds).execute(new String[] {});
    }

    @Test(expected = ExecutePermissionException.class)
    public void executeWithoutPermission(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new Execute(mds).execute(new String[] {app1.getAbsolutePath(), "Lizard poisons", "Spock"});
    }

    @Test
    public void executeNobodyHasPermission(){
        new Login(mds).execute(new String[] {"nobody"});
        new Execute(mds).execute(new String[] {app1.getAbsolutePath(), "Lizard poisons", "Spock"});
    }

    @Test
    public void executeRootHasPermission(){
        new Login(mds).execute(new String[] {"root", "***"});
        new Execute(mds).execute(new String[] {app1.getAbsolutePath(), "Lizard poisons", "Spock"});
    }


//     WRITE
    @Test
    public void writeName(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        Write w = new Write(mds);
        assertEquals("Write Command name isn't write", "write", w.name());
    }

    @Test(expected = RuntimeException.class)
    public void writeWithoutArgs() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Write(mds).execute(new String[] {});
    }

    @Test(expected = RuntimeException.class)
    public void writeOver9000Args() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Write(mds).execute(new String[] {"You", "Shall", "Not", "Pass!!!"});
    }

    @Test(expected = FileDoesNotExistException.class)
    public void writeUnknownFile(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Write(mds).execute(new String[] {"/","I dont even care."});
    }

    @Test(expected = FileDoesNotExistException.class)
    public void writeWithoutPermission(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new Write(mds).execute(new String[] {pf1.getAbsolutePath(),"1-Pennys high pitch laughter"});
    }


//      ENVIRONMENT
    @Test
    public void environmentName(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        Environment e = new Environment(mds);
        assertEquals("Environment Command name isn't env", "env", e.name());
    }

    @Test(expected = RuntimeException.class)
    public void environmentOver9000Args() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Environment(mds).execute(new String[] {"You", "Shall", "Not", "Pass!!!"});
    }

    /*@Test(expected = ExecutePermissionException.class)
    public void environmentWithoutPermission(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new Environment(mds).execute(new String[] {"Green lantern", "lantern"});
    }*/

//      KEY
    @Test
    public void keyName(){
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        Key k = new Key(mds);
        assertEquals("Key Command name isn't token", "token", k.name());
    }

    @Test(expected = RuntimeException.class)
    public void keyOver9000Args() {
        new Login(mds).execute(new String[] {u1.getUsername(),u1.getPassword()});
        new Key(mds).execute(new String[] {"You", "Shall", "Not", "Pass!!!"});
    }
    
    @Test
    public void keyChangeSameUser(){
        new Login(mds).execute(new String[] {u2.getUsername(),u2.getPassword()});
        new Key(mds).execute(new String[] {u2.getUsername()});
    }

    @Test(expected = UserDoesNotExistException.class)
    public void keyInvalidUser(){
        new Key(mds).execute(new String[] {"Angus"});
    }

}





package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.service.*;

import pt.tecnico.mydrive.domain.Session;
import pt.tecnico.mydrive.domain.MyDriveManager;

import pt.tecnico.mydrive.exception.file.access.NotRemovableException;
import pt.tecnico.mydrive.exception.file.access.NotEditableException;
import pt.tecnico.mydrive.exception.permission.WritePermissionException;
import pt.tecnico.mydrive.exception.file.FileDoesNotExistException;


public class WriteFileServiceTest extends AbstractServiceTest {
    
	private long token_1, token_2, token_x;
    private File file;
    private MyDriveManager mydrive;
    private WriteFileService write_file;
    
//populate
    
    @Override
    protected void populate() {
        mydrive = MyDriveManager.getInstance();
        
        User root = mydrive.getUserByUsername("root");
        Directory homeDir = root.getHomeDirectory();
        PlainFile pF = new PlainFile(mydrive,root,homeDir,"pF","This is my content at this moment");
        Link lk = new Link(mydrive,root,homeDir,"lk",pF.getAbsolutePath());
        Application apl = new Application(mydrive,root,homeDir,"apl","this.application.content");
        Directory dir = new Directory(mydrive,root,homeDir,"dir");
        Session s1 = new Session(mydrive,"root","***");
        token_1 = s1.getToken();
        
        User xxuser = new User(mydrive,"xxuser","Xavier Xilofone","xxpassword","rwxd----");
        Directory homeDir_2 = xxuser.getHomeDirectory();
        PlainFile pF_2 = new PlainFile(mydrive,xxuser,homeDir_2,"pF_2","This is another content");
        Link lk_2 = new Link(mydrive,root,homeDir_2,"lk_2",pF_2.getAbsolutePath());
        Application apl_2 = new Application(mydrive,xxuser,homeDir_2,"apl_2","this.application2.content");
        Directory dir_2 = new Directory(mydrive,xxuser,homeDir_2,"dir_2");
        Session s2 = new Session(mydrive,"xxuser","xxpassword");
        token_2 = s2.getToken();
        
        PlainFile pFR = new PlainFile(mydrive,root,homeDir_2,"pFR","This is my content at this moment");
        Link lkR = new Link(mydrive,root,homeDir_2,"lkR",pFR.getAbsolutePath());
        Application aplR = new Application(mydrive,root,homeDir_2,"aplR","this.application.content");
        Directory dirR = new Directory(mydrive,root,homeDir_2,"dirR");
    }
    
//tests

    //Remove PlainFile
    @Test
    public void edit_plainFile_Root_success() throws Exception{
        write_file = new WriteFileService(token_1, "pF", "this is the new content for plain file");
        write_file.execute();
        assertNotNull("token is null", token_1);
                assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/root/pF").getContent());
        assertEquals("Root can remove his own PlainFile file",  "this is the new content for plain file" ,mydrive.getFileByAbsolutePath("/home/root/pF").getContent());
    }
    @Test
    public void edit_plainFile() throws Exception{
        write_file = new WriteFileService(token_2, "pF_2", "this is the new content for plain file");
        write_file.execute();
        assertNotNull("token is null", token_2);
                assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/xxuser/pF_2").getContent());
        assertEquals("A user can remove his own PlainFile file", "this is the new content for plain file",  mydrive.getFileByAbsolutePath("/home/xxuser/pF_2").getContent());
    }
    //Remove Application
    @Test
    public void edit_app_Root_success() throws Exception{
        write_file = new WriteFileService(token_1, "apl", "this is the new content for apl");
        write_file.execute();
        assertNotNull("token is null", token_1);
        assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/root/apl").getContent());
        assertEquals("Root can remove his own Application file", "this is the new content for apl", mydrive.getFileByAbsolutePath("/home/root/apl").getContent());
    }
    @Test
    public void edit_app() throws Exception{
        write_file = new WriteFileService(token_2, "apl_2", "this is the new content for apl_2");
        write_file.execute();
        assertNotNull("token is null", token_2);
        assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/xxuser/apl_2").getContent());
        assertEquals("A user can remove his own Application file", "this is the new content for apl_2", mydrive.getFileByAbsolutePath("/home/xxuser/apl_2").getContent());
    }

     //Remove Link
     @Test
     public void edit_link_Root_success() throws Exception {
         write_file = new WriteFileService(token_1, "lk", "hello");
         write_file.execute();
         assertNotNull("token is null", token_1);
         assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/root/pF").getContent());
         assertEquals("Root can remove his own Link file", "hello", mydrive.getFileByAbsolutePath("/home/root/pF").getContent());
    }

     @Test(expected = WritePermissionException.class)
     public void edit_link() throws Exception {
         write_file = new WriteFileService(token_2, "lk_2", "/home/xxuser/pF_2");
         write_file.execute();
    }
    
    //Remove Directory
    @Test(expected = NotEditableException.class)
    public void editDirectory_Root() throws Exception {
        write_file = new WriteFileService(token_1, "dir", "this is the new content for dir");
        write_file.execute();
    }
    @Test(expected = NotEditableException.class)
    public void editDirectory() throws Exception{
        write_file = new WriteFileService(token_2, "dir_2", "this is the new content for directory");
        write_file.execute();
    }
    
    //Remove other User PlainFile
    @Test
    public void editOtherUserPlainFile_Root() throws Exception{
        write_file = new WriteFileService(token_1, "pF", "this is the new content for PlainFile");
        write_file.execute();
        assertNotNull("token is null", token_1);
        assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/root/pF").getContent());
        assertEquals("Root can remove others users PlainFiles", "this is the new content for PlainFile", mydrive.getFileByAbsolutePath("/home/root/pF").getContent());
    }
    @Test(expected = WritePermissionException.class)
    public void editOtherUserPlainFile() throws Exception{
        write_file = new WriteFileService(token_2, "pFR", "this is the new content for PlainFile");
        write_file.execute();
    }
    //Remove other User Application
    @Test
    public void editOtherUserApplication_Root() throws Exception{
        write_file = new WriteFileService(token_1, "apl", "this is the new content for apl");
        write_file.execute();
        assertNotNull("token is null", token_1);
        assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/root/apl").getContent());
        assertEquals("Root can remove others users Applications", "this is the new content for apl", mydrive.getFileByAbsolutePath("/home/root/apl").getContent());
    }
    @Test(expected = WritePermissionException.class)
    public void editOtherUserApplication() throws Exception{
        write_file = new WriteFileService(token_2, "aplR", "this is the new content for apl");
        write_file.execute();
    }
    /*
    //Remove other User Link
    @Test
    public void editOtherUserLink_Root() throws Exception{
    	mydrive.getSessionByToken(token_1).setCurrentDirectory(homeDir_2);
        write_file = new WriteFileService(token_1, "lkR", "hello");
        write_file.execute();
        assertNotNull("token is null", token_1);
        assertNotNull("path is null", mydrive.getFileByAbsolutePath("/home/xxuser/pFR").getContent());
        assertEquals("Root can remove others users Link", "hello", mydrive.getFileByAbsolutePath("/home/xxuser/pFR").getContent());
    }
    */
    @Test(expected = WritePermissionException.class)
    public void editOtherUserLink() throws Exception{
        write_file = new WriteFileService(token_2, "lkR", "/home/root/pFR");
        write_file.execute();
    }

    //Remove other User Directory
    @Test(expected = NotEditableException.class)
    public void editOtherUserDirectory_Root() throws Exception{
        write_file = new WriteFileService(token_1, "dir", "this is the new content for dir");
        write_file.execute();
    }
    @Test(expected = WritePermissionException.class)
    public void editOtherUserDirectory() throws Exception{
        write_file = new WriteFileService(token_2, "dirR", "this is the new content for dir_2");
        write_file.execute();
    }

    //Remove Non Existing File
    @Test(expected = FileDoesNotExistException.class)
    public void editNonExistingFile() throws Exception{
        write_file = new WriteFileService(token_1,"pF_x" , "this plainfile does not exist");
        write_file.execute();
    }

}

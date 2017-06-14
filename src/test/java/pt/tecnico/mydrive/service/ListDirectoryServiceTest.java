package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.service.dto.FileDto;

import pt.tecnico.mydrive.exception.session.InvalidTokenException;
import pt.tecnico.mydrive.exception.permission.ReadPermissionException;

import java.util.List;

public class ListDirectoryServiceTest extends AbstractServiceTest {
	private long _token1, _token2;
	private Directory _dir;
    private MyDriveManager md;

    protected void populate() {
        md = MyDriveManager.getInstance();

        //User 1
        User u1 = new User(md, "ProChi", "Prof. Chibanga", "pr0ch1...", "rwxd----");
        Session s1 = new Session(md, u1.getUsername(), u1.getPassword());
        _token1 = s1.getToken();
        _dir = s1.getCurrentDirectory();

        new Link(md, u1, _dir, "FastMagic", _dir.getAbsolutePath());
        new Directory(md, u1, _dir, "HermionePics");
        new Application(md, u1, _dir, "LinkSizeReader", "pt.tecnico.mydrive.domain.Link.getSize");
        new Directory(md, u1, _dir, "MagicPotions");
        new PlainFile(md, u1, _dir, "Voodoo", "Recipe for Black Magic Frog Legs");

        //User 2
        User u2 = new User(md, "Dragonborn", "Dark Elf", "arrowInKn33", "rwxd----");
        Session s2 = new Session(md, u2.getUsername(), u2.getPassword());
        _token2 = s2.getToken();
        s2.setCurrentDirectory(_dir);

    }

    @Test
    public void fileDimension() {
        ListDirectoryService service = new ListDirectoryService(_token1);
        service.execute();
        List<FileDto> list = service.result();

     	assertEquals("List without 7 Files", 7, list.size());
     	assertEquals("Link FastMagic has incorrect content size", 12, list.get(2).getDimension());
     	assertEquals("Directory HermionePics has incorrect size", 2, list.get(3).getDimension());
     	assertEquals("App LinkSizeReader has incorrect content size", 38, list.get(4).getDimension());
     	assertEquals("Directory MagicPotions has incorrect size", 2, list.get(5).getDimension());
     	assertEquals("PlainFile Voodoo has incorrect content size", 32, list.get(6).getDimension());
    }

    @Test
    public void fileNames(){
        ListDirectoryService service = new ListDirectoryService(_token1);
        service.execute();
        List<FileDto> list = service.result();

        assertEquals("First file isnt .", ".", list.get(0).getName());
        assertEquals("Second file isnt ..", "..", list.get(1).getName());
        assertEquals("Third file isnt FastMagic", "FastMagic", list.get(2).getName());
        assertEquals("Fourth file isnt HermionePics", "HermionePics", list.get(3).getName());
        assertEquals("Fifth file isnt LinkSizeReader", "LinkSizeReader", list.get(4).getName());
        assertEquals("Sixth file isnt MagicPotions", "MagicPotions", list.get(5).getName());
        assertEquals("Seventh file isnt Voodoo", "Voodoo", list.get(6).getName());
    }

    @Test
    public void ownerName(){
        ListDirectoryService service = new ListDirectoryService(_token1);
        service.execute();
        List<FileDto> list = service.result();

        assertEquals("Link FastMagic has incorrect owner", "ProChi", list.get(2).getOwnerName());
        assertEquals("Directory HermionePics has incorrect owner", "ProChi", list.get(3).getOwnerName());
        assertEquals("App LinkSizeReader has incorrect owner", "ProChi", list.get(4).getOwnerName());
        assertEquals("Directory MagicPotions has incorrect owner", "ProChi", list.get(5).getOwnerName());
        assertEquals("PlainFile Voodoo has incorrect owner", "ProChi", list.get(6).getOwnerName());
    }

    public void emptyDirectory(){
        User u = new User(md, "H@ck3r", "Mr. Robot", "1or0....", "rwxd----");
        Session s = new Session(md ,u.getUsername(),u.getPassword());
        long token = s.getToken();

        ListDirectoryService service = new ListDirectoryService(token);
        service.execute();
        List<FileDto> list = service.result();

        assertEquals("List without 2 Files", 2, list.size());
        assertEquals("First file isnt .", ".", list.get(0).getName());
        assertEquals("Second file isnt ..", "..", list.get(1).getName());
    }

    @Test(expected = InvalidTokenException.class)
    public void invalidToken(){
        long token = 189671;
        ListDirectoryService service = new ListDirectoryService(token);
        service.execute();
    }

    @Test(expected = ReadPermissionException.class)
    public void invalidPermission(){
        ListDirectoryService service = new ListDirectoryService(_token2);
        service.execute();
    }
}


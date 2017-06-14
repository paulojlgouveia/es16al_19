package pt.tecnico.mydrive.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.service.*;

import pt.tecnico.mydrive.exception.session.InvalidTokenException;
import java.util.List;

public class AddVariableServiceTest extends AbstractServiceTest {
    
	private long token_1, token_x;
    
    private String varName = "variable_xpto";
    
    private String value_1 = "1";
    private String value_2 = "2";
    
    private MyDriveManager mydrive;
    
//populate
    @Override
    protected void populate() {
        mydrive = MyDriveManager.getInstance();
        Session session_1 = new Session(mydrive,"root","***");
        token_1 = session_1.getToken();
    }
    
//tests with success

    //Add Variable success
    @Test
    public void add_Variable_success() throws Exception{
        AddVariableService add_variable = new AddVariableService(token_1, varName, value_1);
        add_variable.execute();
        assertNotNull("token is null", token_1);
        assertNotNull("name is null", varName);
        assertNotNull("value is null", value_1);
        assertEquals(1, add_variable.result().size());
        for(String s : add_variable.result()){
            String[] x = s.split("=");
            assertEquals("Add Variable success","1", x[1]);
        }
    }
    
    //Add Variable change success
    @Test
    public void add_Variable_change_success() throws Exception{

        AddVariableService add_variable = new AddVariableService(token_1, varName, value_1);
        add_variable.execute();
        assertNotNull("token is null", token_1);
        assertNotNull("name is null", varName);
        assertNotNull("value is null", value_1);
        for(String s : add_variable.result()){
            String[] x = s.split("=");
            assertEquals("Add Variable success","1", x[1]);
        }
        AddVariableService add_variable_2 = new AddVariableService(token_1, varName, value_2);
        add_variable_2.execute();
        assertNotNull("value_2 is null", value_2);
        for(String s : add_variable.result()){
            String[] x = s.split("=");
            assertEquals("Add Variable success","2", x[1]);
        }
    }

    @Test
    public void addVariableWithName(){
        AddVariableService add_variable = new AddVariableService(token_1, varName);
        add_variable.execute();
        assertTrue("Variable List length is not 0", add_variable.result().size() == 0);
        System.out.println("\n\n\n\n\nWHOOOO\n\n\n\n\n\n");
    }
    
//tests without success
    
    // try to add_variable when token is not valid
    @Test(expected = InvalidTokenException.class)
    public void invalidtoken() throws Exception{
        AddVariableService add_variable = new AddVariableService(token_x, varName, value_1);
        add_variable.execute();
    }
}
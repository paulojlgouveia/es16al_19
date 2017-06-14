package pt.tecnico.mydrive.presentation;

import pt.tecnico.mydrive.service.AddVariableService;

public class Environment extends MdCommand{
    
    public Environment(Shell sh) {
        super(sh, "env", "Create or edit the environment variable");
    }
    
    
    public void execute(String[] args) {
        
        long token;
        String value;
        String varName;
        String variable;
        
        if (args.length == 2){
        	token = ((MyDrive) shell()).getToken();
            AddVariableService  add_variable = new AddVariableService(token, args[0], args[1]);
            add_variable.execute();

            for(String s : add_variable.result()){
            	shell().println("Environment:" + s );
            }
        }
        else if (args.length == 1){
        	token = ((MyDrive) shell()).getToken();
            variable = ((MyDrive) shell()).getVariable();
            AddVariableService  add_variable = new AddVariableService(token);
            add_variable.execute();
            for(String s : add_variable.result()){
            	String[] x = s.split("=");
            	if(x[0].equals(args[0])){
            		shell().println("varName: " + x[1]);
            	}
            }
        }
        else if (args.length == 0){
        	token = ((MyDrive) shell()).getToken();
            AddVariableService  add_variable = new AddVariableService(token);
            add_variable.execute();
            for(String s : add_variable.result()){
            	shell().println("Environment:" + s );
            }
        }
        else{
            throw new RuntimeException("USAGE: " + name() + " <[name [value]]>");
        }
    }
}
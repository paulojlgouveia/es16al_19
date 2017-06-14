package pt.tecnico.mydrive.domain;

import pt.tecnico.mydrive.exception.session.CannotChangeSessionException;

public class Variable extends Variable_Base {
    
    public Variable(Session session, String name, String value) {
    	setName(name);
    	setValue(value);
    	super.setSession(session);
    }
    
    @Override
	public void setSession(Session session) {
    	throw new CannotChangeSessionException(session.getToken());
	}
}

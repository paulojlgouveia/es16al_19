package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.*;
import pt.tecnico.mydrive.exception.session.InvalidTokenException;

import java.util.List;

public class AddVariableService extends MyDriveService {

	private long token;
	private String varName;
	private String value;

  public AddVariableService(long token, String varName, String value) {
    this.token = token;
    this.varName = varName;
    this.value = value;
  }

  public AddVariableService(long token, String varName) {
    this.token = token;
    this.varName = varName;
  }

  public AddVariableService(long token) {
    this.token = token;
  }

  @Override
  public final void dispatch() {
   MyDriveManager mydrive = getMyDriveManager();
   Session session = mydrive.getSessionByToken(token);

   if(session == null)
     throw new InvalidTokenException(token);

   if(session.isValid()) {
     session.renew();
     if(varName != null && value != null)
       session.setVariable(varName, value);
   } else {
     log.warn("Session " + token + " has expired.");
   }
 }

 public final List<String> result() {
   MyDriveManager mydrive = getMyDriveManager();
   Session session = mydrive.getSessionByToken(token);

    session.renew();
    return session.getVariableList();

 }
}

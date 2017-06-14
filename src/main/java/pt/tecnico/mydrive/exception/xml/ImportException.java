package pt.tecnico.mydrive.exception.xml;

public class ImportException extends XMLException {

	private static final long serialVersionUID = 1L;

	
// 	public ImportException() { super();}


	public ImportException(String problemClass, String objectName) {
		super(problemClass, objectName);
	}

	public ImportException(String problemClass, String objectName, String error) {
		super(problemClass, objectName, error);
	}


	@Override
	public String getMessage(){
		return "XML Import: " + super.getMessage();
	}
}
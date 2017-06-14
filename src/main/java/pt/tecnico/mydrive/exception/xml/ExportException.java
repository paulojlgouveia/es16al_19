package pt.tecnico.mydrive.exception.xml;

public class ExportException extends XMLException {

	private static final long serialVersionUID = 1L;
	
	
	public ExportException(String problemClass, String object) {
		super(problemClass, object);
	}
	
	public ExportException(String problemClass, String object, String error) {
		super(problemClass, object, error);
	}
	
	@Override
	public String getMessage() {
		if(!getInsideError().equals("")) {
			return "XML Error exporting " + getClassName() + " : " + getObjectName() + " : "  + getInsideError();
		}
		else {
			return "XML Error exporting " + getClassName() + " : " + getObjectName();
		}	
	}
}

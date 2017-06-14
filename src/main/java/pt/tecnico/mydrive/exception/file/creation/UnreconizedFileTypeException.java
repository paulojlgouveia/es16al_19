package pt.tecnico.mydrive.exception.file.creation;


public class UnreconizedFileTypeException extends FileCreationException {

	private static final long serialVersionUID = 1L;
	private String fileType;
	
	public UnreconizedFileTypeException(String filename, String fileType) {
		super(filename);
		this.fileType = fileType;
	}
	
	
	public String getFileType() { return fileType; }
	
	@Override
	public String getMessage() {
		return "Failed to create: " + getFileName() + "\n" + getFileType() + " is not a recognised file type.";
	}
}

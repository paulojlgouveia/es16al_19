package pt.tecnico.mydrive.exception.file.creation;


public class PathTooLongException extends FileCreationException {

	private static final long serialVersionUID = 1L;
	private int pathLength;
	
	public PathTooLongException(String name, int pathLength) {
		super(name);
		this.pathLength = pathLength;
	}
	
	public int getPathLength() { return pathLength; }
	
	
	@Override
	public String getMessage() {
		return "Failed to create: " + getFileName()
				+ "\nAbsolute path exceeds length limit by " + (pathLength-1024);
	}
}

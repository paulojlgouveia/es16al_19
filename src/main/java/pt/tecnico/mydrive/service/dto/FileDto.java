package pt.tecnico.mydrive.service.dto;

import org.joda.time.DateTime;

public class FileDto implements Comparable<FileDto> {

    private String fileType;
    private String permissions;
    private int dimension;
    private String ownerName;
    private int id;
    private DateTime lastModified;
    private String name;
    private String parentPath;
    private String content;

    public FileDto(String fileType, String permissions, int dimension, 
    		       String ownerName, int id, String name) {
        this.fileType = fileType;
        this.permissions = permissions;
        this.dimension = dimension;
        this.ownerName = ownerName;
        this.name = name;
    }

    public FileDto(String fileType, String permissions, int dimension, 
	               String ownerName, int id, String name, DateTime lastModified) {
        this(fileType, permissions, dimension, ownerName, id, name);
	    this.lastModified = lastModified;
    }
    //Used for links only
    public void setContent(String content){
    	this.content = content;
    }
   //Used for links only
    public String getContent(){
    	return this.content;
    }

    public String getFileType(){
    	return this.fileType;
    }
    
    public String getPermissions(){
    	return this.permissions;
    }
    
    public int getDimension(){
    	return this.dimension;
    }
    
    public String getOwnerName(){
    	return this.ownerName;
    }
    
    public int getId(){
    	return this.id;
    }
    
    public DateTime getLastModified(){
    	return this.lastModified;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public String getLastModifiedString(){
    	return this.lastModified.toString();
    }

    @Override
    public int compareTo(FileDto other) {
    	return name.compareTo(other.getName());
    }
}

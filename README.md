# MyDrive Application

This is the main README file for the MyDrive project.

### Repository Structure
The current version of the repository contains the following structure:
	
	.
	├── import.xml
	├── info
	│   ├── ist175657.dml
	│   ├── ist176213.dml
	│   ├── ist70969.dml
	│   ├── ist73422.dml
	│   ├── ist75694.dml
	│   ├── ist78437.dml
	│   └── mydrive.png
	├── mydriveExport.xml
	├── pom.xml
	├── README.md
	└── src
		├── main
		│   ├── dml
		│   │   └── mydrive.dml
		│   ├── java
		│   │   └── pt
		│   │       └── tecnico
		│   │           └── mydrive
		│   │               ├── domain
		│   │               │   ├── Application.java
		│   │               │   ├── Association.java
		│   │               │   ├── Directory.java
		│   │               │   ├── File.java
		│   │               │   ├── GuestUser.java
		│   │               │   ├── Link.java
		│   │               │   ├── MyDrive.java
		│   │               │   ├── PlainFile.java
		│   │               │   ├── Session.java
		│   │               │   ├── SuperUser.java
		│   │               │   ├── User.java
		│   │               │   └── Variable.java
		│   │               ├── exception
		│   │               │   ├── file
		│   │               │   │   ├── access
		│   │               │   │   │   ├── FileAccessException.java
		│   │               │   │   │   ├── NotDirectoryException.java
		│   │               │   │   │   ├── NotEditableException.java
		│   │               │   │   │   ├── NotExecutableException.java
		│   │               │   │   │   ├── NotReadableException.java
		│   │               │   │   │   └── NotRemovableException.java
		│   │               │   │   ├── creation
		│   │               │   │   │   ├── DuplicateFileException.java
		│   │               │   │   │   ├── EmptyLinkException.java
		│   │               │   │   │   ├── FileCreationException.java
		│   │               │   │   │   ├── InvalidFileNameException.java
		│   │               │   │   │   ├── InvalidLinkException.java 
		│   │               │   │   │   ├── InvalidPathException.java
		│   │               │   │   │   ├── PathTooLongException.java
		│   │               │   │   │   └── UnreconizedFileTypeException.java
		│   │               │   │   ├── Execution
		│   │               │   │   │   ├── FileExecutionException.java
		│   │               │   │   │   ├── InvalidClassException.java
		│   │               │   │   │   └── InvalidMethodException.java
		│   │               │   │   └── FileDoesNotExistException.java
		│   │               │   ├── login
		│   │               │   │   ├── LoginException.java
		│   │               │   │   ├── UserNotInSessionException.java
		│   │               │   │   └── WrongPasswordException.java
		│   │               │   ├── MyDriveException.java
		│   │               │   ├── permission
		│   │               │   │   ├── DeletePermissionException.java
		│   │               │   │   ├── ExecutePermissionException.java
		│   │               │   │   ├── PermissionException.java
		│   │               │   │   ├── ReadPermissionException.java
		│   │               │   │   └── WritePermissionException.java
		│   │               │   ├── session
		│   │               │   │   ├── CannotChangeSessionException.java
		│   │               │   │   ├── CannotChangeTokenFromSessionException.java
		│   │               │   │   ├── CannotChangeUserFromSessionException.java
		│   │               │   │   ├── InvalidTokenException.java
		│   │               │   │   └── SessionException.java
		│   │               │   ├── user
		│   │               │   │   ├── access
		│   │               │   │   │   ├── CannotChangeUserPasswordException.java
		│   │               │   │   │   ├── GuestUserRemovalException.java
		│   │               │   │   │   ├── RootRemovalException.java
		│   │               │   │   │   └── UserAccessException.java
		│   │               │   │   ├── creation
		│   │               │   │   │   ├── DuplicateUsernameException.java
		│   │               │   │   │   ├── InvalidUsernameException.java
		│   │               │   │   │   └── UserCreationException.java
		│   │               │   │   ├── PasswordLengthException.java
		│   │               │   │   ├── UserDoesNotExistException.java
		│   │               │   │   └── UserIsNotInSessionException.java
		│   │               │   └── xml
		│   │               │       ├── ExportException.java
		│   │               │       ├── ImportException.java
		│   │               │       └── XMLException.java
		│   │               ├── MyDriveApplication.java
		│   │               ├── presentation
		│   │               │   ├── ChangeDirectory.java
		│   │               │   ├── Command.java
		│   │               │   ├── Environment.java
		│   │               │   ├── Execute.java
		│   │               │   ├── Key.java
		│   │               │   ├── List.java
		│   │               │   ├── Login.java
		│   │               │   ├── MdCommand.java
		│   │               │   ├── MyDrive.java
		│   │               │   ├── Shell.java
		│   │               │   ├── Sys.java
		│   │               │   └── Write.java
		│   │               └── service
		│   │                   ├── AddVariableService.java
		│   │                   ├── ChangeDirectoryService.java
		│   │                   ├── CreateFileService.java
		│   │                   ├── DeleteFileService.java
		│   │                   ├── dto
		│   │                   │   └── FileDto.java
		│   │                   ├── ExecuteFileService.java
		│   │                   ├── FileType.java
		│   │                   ├── ListDirectoryService.java
		│   │                   ├── LoginUserService.java
		│   │                   ├── MyDriveService.java
		│   │                   ├── ReadFileService.java
		│   │                   └── WriteFileService.java
		│   └── resources
		│       ├── fenix-framework-jvstm-ojb.properties
		│       ├── fenix-framework-jvstm-ojb-sigma.properties
		│       └── log4j.properties
		└── test
			└── java
				└── pt
					└── tecnico
						└── mydrive
							├── service
							│   ├── AbstractServiceTest.java
							│   ├── AddVariableServiceTest.java
							│   ├── ChangeDirectoryServiceTest.java
							│   ├── CreateFileServiceTest.java
							│   ├── DeleteFileServiceTest.java
							│   ├── ExecuteFileServiceTest.java
							│   ├── ListDirectoryServiceTest.java
							│   ├── LoginUserServiceTest.java
							│   ├── ReadFileServiceTest.java
							│   └── WriteFileServiceTest.java
							└── system
								├── IntegrationTest.java
								└── SystemTest.java
								
		
## Usefull commands

mvn dbclean:dbclean

mvn clean

mvn clean compile package

mvn exec:java



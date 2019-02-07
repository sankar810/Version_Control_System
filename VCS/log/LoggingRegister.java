package edu.csulb.ase.scm.log;

/**
 *  This file is a data class and keeps track of each of the files artifact Id file name and file path.
 * @author Tejas Tundulwar(ttejas@rocketmail.com)
 * @version 1.0
 */
public class LoggingRegister {
	private String artifactID = "";
	private String file_Name = "";
	private String file_Path = "";

	public LoggingRegister(String artifactID, String file_Name, String file_Path) {
		this.artifactID = artifactID;
		this.file_Name = file_Name;
		this.file_Path = file_Path;
	}
	
	public String getfile_Name() {
		return file_Name;
	}
	
	public void setfile_Name(String file_Name) {
		this.file_Name = file_Name;
	}
	
	public String getArtifactName() {
		return artifactID;
	}
	
	public void setArtifactName(String artifactID) {
		this.artifactID = artifactID;
	}
	
	public String getfile_Path() {
		return file_Path;
	}
	
	public void setfile_Path(String file_Path) {
		this.file_Path = file_Path;
	}		
}
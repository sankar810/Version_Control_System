package com.csulb.cecs543.vcs.Log;

public class LoggingHelper {
	private String artifactId = "";
	private String fName = "";
	private String fWithPath = "";

	public LoggingHelper(String artifactID, String fileName, String fileWithPath) {
		this.artifactId = artifactID;
		this.fName = fileName;
		this.fWithPath = fileWithPath;
	}

	public String getArtifactId() {
		return artifactId;
	}
	
	public void setArtifactId(String artifactID) {
		this.artifactId = artifactID;
	}
	
	public String getFileName() {
		return fName;
	}
	
	public void setFileName(String fileName) {
		this.fName = fileName;
	}
	
	public String getFileWithPath() {
		return fWithPath;
	}
	
	public void setFileWithPath(String fileWithPath) {
		this.fWithPath = fileWithPath;
	}		
}

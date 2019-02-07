package edu.csulb.ase.scm.log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used for logging. The log method creates the manifest file.
 * @author Vaibhav Jain(vaibhav8121992@gmail.com)
 * @version 1.0
 */
public class Log {
    private Log () {
    }

    public static Log getInstance() {
        return new Log();
    }

	/**
	 * A new Manifest file is created with date/time attached. This file contains all the files that were affected by the command passed.
	 * @param command - The command to be logged.
	 * @param sourcePath - Source path of directory
	 * @param repoPath - The destination path
	 * @param LoggingRegister - An ArrayList of all the classes that were copied/affected by the command
	 * @throws IOException If an I/O error occurs or the parent directory does not exist
	 */
	public void log(String command, String sourcePath, String repoPath, List<LoggingRegister> LoggingRegister) throws IOException{
		ArrayList<String> lines = new ArrayList<String>();
		LocalDateTime currentDateTime = LocalDateTime.now();
		String timePath = String.valueOf(currentDateTime.getHour() + "." + currentDateTime.getMinute() + "." + currentDateTime.getSecond()) + " Hrs";
		String fileName = "Manifest"+ currentDateTime.toLocalDate() + " - " +timePath+ ".txt";
		Path manifestPath = FileSystems.getDefault().getPath(repoPath + FileSystems.getDefault().getSeparator() + "Manifest" 
				+ FileSystems.getDefault().getSeparator() + fileName);
		Charset charset = StandardCharsets.US_ASCII;
		
		CreateManifestDir(repoPath);	//Verify the Manifest dir
		Files.createFile(manifestPath);
		lines.add("Version - ");
		lines.add("Timestamp : "+ currentDateTime.toLocalDate() +" - " +timePath);
		lines.add("Source : " + sourcePath);
		lines.add("Destination/Repo-Path : " + repoPath);
		lines.add("Command: " + (command + " " +sourcePath + " " +repoPath));
		lines.add("Files created/copied: ");
		LoggingRegister.forEach((helper) -> {
			String line = helper.getArtifactName() + "\t" + helper.getfile_Name() + "\t" + helper.getfile_Path();
			lines.add(line);
		});
		Files.write(manifestPath, lines, charset, StandardOpenOption.APPEND);
	}
	
	/**
	 * Creates an "Activity" directory if it is not present at the path that is specified.
	 * @param pathToRepo - The path to verify/create the activity directory at.
	 * @throws IOException  If an I/O error occurs or the parent directory does not exist.
	 */
	public void CreateManifestDir(String pathToRepo) throws IOException{
		Path path = FileSystems.getDefault().getPath(pathToRepo + FileSystems.getDefault().getSeparator() + "Manifest");
		if(!Files.exists(path)){
			Files.createDirectory(path);
		}
	}
}
package com.ase.vcs;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * This creates the manifest file and log's every command entered.
 * @author Sankaran Shanmugavelayudam(sankaran22.5@gmail.com)
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
	 * @param cmd - The command to be logged.
	 * @param srcPath - Source path of directory
	 * @param repPath - The destination path
	 * @param LoggingRegister - An ArrayList of all the classes that were copied/affected by the command
	 * @throws IOException If an I/O error occurs or the parent directory does not exist
	 */
	public void log(String cmd, String srcPath, String repPath, List<LoggingRegister> LoggingRegister) throws IOException{
		ArrayList<String> entry = new ArrayList<String>();
		LocalDateTime Date_Time = LocalDateTime.now();
		String currentTime = String.valueOf(Date_Time.getHour() + "." + Date_Time.getMinute() + "." + Date_Time.getSecond()) + " Hrs";
		String fileName = "Manifest "+ Date_Time.toLocalDate() + " - " +currentTime+ ".txt";
		FileSystem manifest =  FileSystems.getDefault();
		Path manifestFilePath = manifest.getPath(repPath + FileSystems.getDefault().getSeparator() + "Manifest" 
				+ FileSystems.getDefault().getSeparator() + fileName);
		Charset charset = StandardCharsets.US_ASCII;
		
		CreateManifestDir(repPath);
		Files.createFile(manifestFilePath);
		entry.add("Timestamp : "+ Date_Time.toLocalDate() +" - " +currentTime);
		entry.add("Source : " + srcPath);
		entry.add("Destination/Repo-Path : " + repPath);
		entry.add("command: " + (cmd + " " +srcPath + " " +repPath));
		entry.add("Files created/copied: ");
		
		for (LoggingRegister reg : LoggingRegister) {
			String line = reg.getArtName() + "\t" + reg.getfile_Name() + "\t" + reg.getfile_Path();
			entry.add(line);
		}

		Files.write(manifestFilePath, entry, charset, StandardOpenOption.APPEND);
	}
	
	/**
	 * Creates an "Activity" directory if it is not present at the path that is specified.
	 * @param pathToRepo - The path to verify/create the activity directory at.
	 * @throws IOException  If an I/O error occurs or the parent directory does not exist.
	 */
	public void CreateManifestDir(String pathToRepo) throws IOException{
		FileSystem manifestfs = FileSystems.getDefault();
		Path path = manifestfs.getPath(pathToRepo + FileSystems.getDefault().getSeparator() + "Manifest");
		
		if(!Files.exists(path)){
			Files.createDirectory(path);
		}
	}
}
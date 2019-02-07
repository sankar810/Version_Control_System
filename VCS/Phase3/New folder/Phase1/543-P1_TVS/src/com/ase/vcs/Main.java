package com.ase.vcs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * The main program executes first and checks for the command and logs the command.
 * @author Priya Veerapaneni(priya.veerapaneni3@gmail.com)
 *
 */
public class Main {
    private static final Log LOG = Log.getInstance();
    
	private static Scanner sc;

    public static void main(String[] args) throws IOException {
    	
    	sc = new Scanner(System.in);
    	System.out.println("Usage:"+ System.lineSeparator() + "Please enter source and destination path" + System.lineSeparator());
    	
    	cmdCreate(); 
    }
    /**
     * This method gets the source and destination path. ptreewalker.java walks the project tree structure and copies the file to the destination directory
     * this method also created the manifest file using log.java
     * @throws IOException
     */
    private static void cmdCreate() throws IOException {
        String source, destination;

        System.out.println("Source path:"); //get source path
        source = sc.next();
        System.out.println("Destination path:"); //get destination path
        destination = sc.next();

        PTreeWalker ptreewalker = new PTreeWalker(source,destination); //call projecttreewalker giving source and destination as parameters
        Path path = Paths.get(source);
        Files.walkFileTree(path, ptreewalker); //walk filetree in the given path

        List<LoggingRegister> loggingRegisters = ptreewalker.getLogfileList();
        LOG.log("create", source, destination, loggingRegisters); //Saves the command CREATE to the manifest file
        
        System.out.println(" \n Repository Created Successfully at : " + destination + "\n");

    }
}

package com.ase.vcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.lang.Object;
//import org.apache.commons.io.FileUtils;
import java.util.Arrays;
import java.util.Comparator;

/**
 * The main program executes first and checks for the command and logs the command.
 * @author Priya Veerapaneni(priya.veerapaneni3@gmail.com)
 *
 */
public class Main extends SimpleFileVisitor<Path>{
    private static final Log LOG = Log.getInstance();
    
	private static Scanner sc;
	
    private static String source, destination;
    
    private static int input_Command;
    private static Scanner scSelectInput;
    private static Main m;
	static Scanner scLabel;

	static Scanner scLabelInput;

    public static void main(String[] args) throws IOException {
    
    	sc = new Scanner(System.in);
    	System.out.println("Usage:"+ System.lineSeparator() + "Please enter source and destination path" + System.lineSeparator());

        System.out.println("Source path:"); //get source path
        source = sc.next();
        System.out.println("Destination path:"); //get destination path
        destination = sc.next();

        scSelectInput  = new Scanner(System.in);
        System.out.println(" \n Select command : \n 1. Create \n 2. Check-In \n 3. Check-Out ");
        System.out.println(" 4. Merge\n");
        input_Command= scSelectInput.nextInt();
        
        switch (input_Command) {
        case 1:
        	cmdCreate(); 
        	break;
        case 2:
            cmdCheckin();
            break;
        case 3:
            cmdCheckOut();
            break;
        case 4:
            cmdMerge();
            break;
        default:
            System.out.println(" \n Invalid Input ");
            } 	
    }
    /**
     * This method gets the source and destination path. ptreewalker.java walks the project tree structure and copies the file to the destination directory
     * this method also created the manifest file using log.java
     * @throws IOException
     */
    private static void cmdCreate() throws IOException {

        PTreeWalker ptreewalker = new PTreeWalker(source,destination); //call projecttreewalker giving source and destination as parameters
        Path path = Paths.get(source);
        Files.walkFileTree(path, ptreewalker); //walk filetree in the given path

        List<LoggingRegister> loggingRegisters = ptreewalker.getLogfileList();
        LOG.log("Create", source, destination, loggingRegisters); //Saves the command CREATE to the manifest file
        
        System.out.println(" \n Repository Created Successfully at : " + destination + "\n");

    }
    
    private static void cmdCheckin() throws IOException {
        if(m == null) {
            m = new Main();
        }
        PTreeWalker ptreewalker = new PTreeWalker(source,destination);
        Path path = Paths.get(Main.source);
        try {
        Files.walkFileTree(path, ptreewalker);
        }
        catch(IOException e){
        	e.printStackTrace();
        }
        
        List<LoggingRegister> loggingRegisters = ptreewalker.getLogfileList();
        LOG.log("Checkin", source, destination, loggingRegisters);
        
        System.out.println(" \n Repository Checked-In Successfully at : " + destination + "\n");
    }
    
	private static void cmdCheckOut() throws IOException {
 
        String[] manifestFiles = new File(source + File.separatorChar + "Manifest" + File.separatorChar).list();
        System.out.println("Select the manifest to checkout: " + manifestFiles.length);
        
        
        for (int i = 0; i < manifestFiles.length; i++) {
            System.out.printf("%d.\t%s\n", i+1, manifestFiles[i]);
        }
 
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        File manifestFile = new File(source + File.separatorChar + "Manifest" + File.separatorChar + manifestFiles[choice]);
 
        List<LoggingRegister> logHelpers = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(manifestFile))){
        	bufferedReader.readLine();
        	bufferedReader.readLine();
        	bufferedReader.readLine();
        	bufferedReader.readLine();
        	bufferedReader.readLine();
        	/*System.out.println(bufferedReader.readLine());
 */
            System.out.println(bufferedReader.ready());
            
            
            while (bufferedReader.ready()) {
                String []entry = bufferedReader.readLine().split("\t");
                //System.out.println(Arrays.toString(entry));
                logHelpers.add(new LoggingRegister(entry[0], entry[1], entry[2]));
 
                if (entry[0].isEmpty()) continue;

                Path destFile = Paths.get(destination + File.separatorChar + entry[1]);
                destFile.getParent().toFile().mkdirs();
 
                //System.out.println("DESTFILE"+ destFile.toString());
                
                Files.copy(Paths.get( entry[2]), destFile, StandardCopyOption.REPLACE_EXISTING);            
                
            }
        } 
        System.out.println(" \n Repository Checked-Out Successfully at : " + destination + "\n");
        PTreeWalker ptreewalker = new PTreeWalker(source,destination);
        List<LoggingRegister> loggingRegisters = ptreewalker.getLogfileList();
        LOG.log("Checkout", source, destination, loggingRegisters);
    }
	
private static void cmdMerge() throws IOException {
	
	List<String> conflictPathlist= Merger.mergeHandler(source, destination);

	if(conflictPathlist.size()==0){
		System.out.println("No conflict");
	}
	else{
		System.out.println("\nList of full paths where the conflicted files are merged:");
		for(int i=0;i<conflictPathlist.size();i++){
			System.out.println("   "+conflictPathlist.get(i));
		}
	}
	
        
    }
    /*
    public static void getRepoParent(String []entry, String []entry2, String repoParent, String treeParent, String fileName, String extension) throws Exception{
        
        try(BufferedReader bufferedReader3 = new BufferedReader(new FileReader(source + File.separatorChar + "Activity" + File.separatorChar + "Parent_Name.Log"))){
            while(bufferedReader3.ready()) {
                String []compare = bufferedReader3.readLine().split(":");
                if(compare[0].equals(entry[0]) && compare[2].equals(entry[1])) {
                    repoParent=compare[1];
                }
                if(compare[0].equals(entry2[0]) && compare[2].equals(entry2[1])){
                    treeParent = compare[1];
                }
                if(repoParent!=null && repoParent.equals(treeParent)) {
                    Files.copy(Paths.get(source + File.separatorChar + compare[2] + File.separatorChar + repoParent), 
                    Paths.get(destination + File.separatorChar + fileName + "_MG." + extension), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println(" \n Merge Completed Successfully ");
                    return;
                }
                else if (repoParent!=null && repoParent.equals(entry2[0]) && compare[2].equals(entry2[1])) {
                    Files.copy(Paths.get(source + File.separatorChar + compare[2] + File.separatorChar + repoParent), 
                    Paths.get(destination + File.separatorChar + fileName + "_MG." + extension), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println(" \n Merge Completed Successfully ");
                    return;
                }
            }
            entry[0]=repoParent;
            getRepoParent(entry, entry2, repoParent, treeParent ,fileName, extension);
        }
      */  
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }


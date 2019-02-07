package com.csulb.cecs543.vcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

//import com.csulb.cecs543.vcs.Log.Logging;
import com.csulb.cecs543.vcs.Log.LoggingHelper;


public class Main extends SimpleFileVisitor<Path>{

    private Stack<Path> targetStack = new Stack<>();
    private static ArrayList<LoggingHelper> logFList = new ArrayList<>();
	private static long[] weightChecksum = { 1, 3, 7, 11, 17 }; // Weights for calculating weighted checksum
    private static String source;
    private static String destination;
    private static String Label;
    private static int input_Command, labelDecision;
    private static Main m;
	private static Scanner sc, scSelectInput, scLabel, scLabelInput;

    public static void main(String[] args) throws IOException {
        sc = new Scanner(System.in);
        System.out.println("Please Enter Source path:");
        source = sc.next();
        System.out.println("Please Enter Destination path:");
        destination = sc.next();
        
        scSelectInput  = new Scanner(System.in);
        System.out.println(" \n Select command : \n 1. Create \n 2. Check-In \n 3. Check-Out \n 4. Merge");
        input_Command= scSelectInput.nextInt();
        
        switch (input_Command) {
        case 1:
        	cmdCreate();
        	System.out.println(" \n Repository Created Successfully at : " + destination + "\n");
            break;
        case 2:
        	cmdCheckin(args);
        	System.out.println(" \n Repository Checked-In Successfully at : " + destination + "\n");
            break;
        case 3:
        	cmdCheckOut(args);
        	System.out.println(" \n Repository Checked-Out Successfully at : " + destination + "\n");
            break;
        case 4:
        	cmdMerge();
        	break;
        default:
        	System.out.println(" \n Invalid Input ");
        	}
    }
  
    private static void cmdCreate() throws IOException {

        Path path = Paths.get(Main.source);
        m = new Main();
        Files.walkFileTree(path, m);
        log("Create", Main.logFList);
    }
    
    private static void cmdCheckin(String[] args) throws IOException {
        if(m == null) {
        	m = new Main();
        }

        Path path = Paths.get(Main.source);
        Files.walkFileTree(path, m);

        log("Checkin", logFList);
    }
    
    private static void cmdCheckOut(String[] args) throws IOException {

        String[] manifestFiles = new File(source + File.separatorChar + "Activity" + File.separatorChar).list();
        System.out.println("Select the manifest to checkout: " + manifestFiles.length);
        for (int i = 0; i < manifestFiles.length; i++) {
            System.out.printf("%d.\t%s\n", i+1, manifestFiles[i]);
        }

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        File manifestFile = new File(source + File.separatorChar + "Activity" + File.separatorChar + manifestFiles[choice]);

        List<LoggingHelper> logHelpers = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(manifestFile))){
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();

            while (bufferedReader.ready()) {
                String []entry = bufferedReader.readLine().split("\t");
                logHelpers.add(new LoggingHelper(entry[0], entry[1], entry[2]));

                if (entry[0].isEmpty()) continue;

                Path destFile = Paths.get(destination + File.separatorChar + entry[2].replace(File.separatorChar + entry[0], ""));
                destFile.getParent().toFile().mkdirs();

                Files.copy(Paths.get(source + File.separatorChar + entry[2]), destFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        
        log("Checkout", logHelpers);
    }
    
    private static void cmdMerge() throws IOException {
    	
    	
    	String []entry=null;
    	String []entry2=null;
    	String []compare=null;
    	String repoParent=null;
    	String treeParent=null;
//    	File logFile = new File(source + File.separatorChar + "Activity" + File.separatorChar + "Parent_Name.Log");
    	
    	
    	String[] manifestFiles = new File(source + File.separatorChar + "Activity" + File.separatorChar).list();
        System.out.println("Select the Repo manifest to check: " + manifestFiles.length);
        for (int i = 0; i < manifestFiles.length; i++) {
            System.out.printf("%d.\t%s\n", i+1, manifestFiles[i]);
        }

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt() - 1;
        File manifestFile = new File(source + File.separatorChar + "Activity" + File.separatorChar + manifestFiles[choice]);

//        List<LoggingHelper> logHelpers = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(manifestFile));
        try{
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();

           /* while (bufferedReader.ready()) {
                entry = bufferedReader.readLine().split("\t");
            }*/
            
        } catch (Exception e) {
			// TODO: handle exception
		}
    	
        String[] manifestFiles2 = new File(destination + File.separatorChar + "Activity" + File.separatorChar).list();
        System.out.println("Select the project tree manifest to check: " + manifestFiles2.length);
        for (int i = 0; i < manifestFiles2.length; i++) {
            System.out.printf("%d.\t%s\n", i+1, manifestFiles2[i]);
        }

        Scanner scanner2 = new Scanner(System.in);
        int choice2 = scanner2.nextInt() - 1;
        File manifestFile2 = new File(destination + File.separatorChar + "Activity" + File.separatorChar + manifestFiles2[choice2]);

//        List<LoggingHelper> logHelpers2 = new ArrayList<>();
        try(BufferedReader bufferedReader2 = new BufferedReader(new FileReader(manifestFile2))){
            bufferedReader2.readLine();
            bufferedReader2.readLine();
            bufferedReader2.readLine();
            bufferedReader2.readLine();
            bufferedReader2.readLine();
            bufferedReader2.readLine();

            while (bufferedReader2.ready() && bufferedReader.ready()) {
                entry2 = bufferedReader2.readLine().split("\t");
                entry = bufferedReader.readLine().split("\t");
                if (!entry[0].equals(entry2[0])) {
                	String extension = "";
                	String fileName="";
                    int i = entry2[1].lastIndexOf('.');
                    if (i > 0) {
                    	fileName = entry2[1].substring(0,i);
                    	extension = entry2[1].substring(i+1);
                    	Path sourceFile = Paths.get(destination + File.separatorChar + entry2[1]);
                    	Path destFile = Paths.get(destination + File.separatorChar + fileName + "_MT." + extension);
                    	Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
                    	Files.deleteIfExists(sourceFile);
                    	Files.copy(Paths.get(source + File.separatorChar + entry[1] + File.separatorChar + entry[0]), Paths.get(destination + File.separatorChar + fileName + "_MR." + extension), StandardCopyOption.REPLACE_EXISTING);
                    	//call
                    	
                    	try {
							getRepoParent(entry, entry2, repoParent, treeParent, fileName, extension);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
                else {
                	System.out.println(" \n No Difference Found ");
                }
            }
            
        }
        
    }
    
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
    	
    	
	}
    
    @Override
    public FileVisitResult visitFile(Path srcFPath, BasicFileAttributes basicFAttributes) {

        if (basicFAttributes.isRegularFile()) {
            String fName = srcFPath.getFileName().toString();
            String artifactId = Main.getArtifactName(srcFPath.toFile());

            Path targetPath = targetStack.peek().resolve(fName);
            targetPath.toFile().mkdirs();
            targetPath = targetPath.resolve(artifactId);

            try {
                if (!targetPath.toFile().exists()) {
                	logFList.add(new LoggingHelper(artifactId, fName, targetPath.toString().replace(destination, "")));
                    Files.copy(srcFPath, targetPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
        String folderName = dir.toString();
        folderName = folderName.replace(source, destination);
        targetStack.push(Paths.get(folderName));
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path path, IOException ioException) {
        targetStack.pop();
        return FileVisitResult.CONTINUE;
    }
    
    /**
	 * A new Manifest file is created with date/time attached. This file contains all the files details that were copied.
	 * @param command - The command to be logged.
	 * @param source - Source path of directory
	 * @param destination - The destination path
	 * @param loggingHelper - An ArrayList of all the files that were copied by the command
	 */
	public static void log(String command, List<LoggingHelper> loggingHelper) throws IOException{
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> logList = new ArrayList<String>();
		LocalDateTime dateTime = LocalDateTime.now();
		String time = String.valueOf(dateTime.getHour() + "." + dateTime.getMinute() + "." + dateTime.getSecond()) + " Hrs";
		String manifestName=null;
		
		if(command.equals("Create")  || command.equals("Checkin")) {
		
			System.out.println("Do you want to Enter Label: \n 1. Yes \n 2. No ");
			scLabel=new Scanner(System.in);
			labelDecision=scLabel.nextInt();
			
		if(labelDecision==1){
			
			System.out.println(" Enter Label ");
			scLabelInput=new Scanner(System.in);
			Label=scLabelInput.nextLine();
			manifestName = "Manifest " + dateTime.toLocalDate() + " -- " +time+ " -- " + Label + ".txt";
		}
		
		else {
			manifestName = "Manifest " + dateTime.toLocalDate() + " -- " +time+ ".txt";
		}
		}
		else {
			manifestName = "Manifest " + dateTime.toLocalDate() + " -- " +time+ ".txt";
		}
		
		Path manifestPath = FileSystems.getDefault().getPath(destination + FileSystems.getDefault().getSeparator() + "Activity" 
				+ FileSystems.getDefault().getSeparator() + manifestName);
		Path logPath = FileSystems.getDefault().getPath(destination + FileSystems.getDefault().getSeparator() + "Activity" 
				+ FileSystems.getDefault().getSeparator() + "Parent_Name.Log");
		
		Charset chset = StandardCharsets.US_ASCII;
		
		CreateActivityDir(destination);
		Files.createFile(manifestPath);
		list.add("XIV");
		list.add("Timestamp : " + dateTime.toLocalDate() + " : " +time);
		list.add("Command : " + (command));
		list.add("Source : " + source);
		list.add("Destination/Repo-Path : " + destination);
		list.add("Files created : ");
		
		for (LoggingHelper help : loggingHelper) {
			String line = help.getArtifactId() + "\t" + help.getFileName() + "\t" + help.getFileWithPath();
			list.add(line);
			
			
			if(command.equals("Create")) {
				String logLine = help.getArtifactId() + ":" + help.getArtifactId() + ":" + help.getFileName();
				logList.add(logLine);
				
				if(!Files.exists(logPath)) {
				Files.createFile(logPath);
				}
			}
			
			else if (command.equals("Checkin")) {
				
				String []fileData;
				File logFile = new File(destination + File.separatorChar + help.getFileName());
				File[] fileList = logFile.listFiles();
				File parentFile = new File(destination + File.separatorChar + "Activity" + File.separatorChar + "Parent_Name.Log");
		        BufferedReader bufferedReader = new BufferedReader(new FileReader(parentFile));
				Arrays.sort(fileList, new Comparator<File>(){
				    public int compare(File f1, File f2)
				    {
				        //return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
				    	if(Long.valueOf(f1.lastModified()) - Long.valueOf(f2.lastModified()) > 0)
				    		return -1;
				    	if(Long.valueOf(f1.lastModified()) - Long.valueOf(f2.lastModified()) < 0)
				    		return 1;
				    	else 
				    		return 0;
				    } });
				try{
		            
		            while (bufferedReader.ready()) {
		                fileData = bufferedReader.readLine().split(":");
		                if(help.getFileName().equals(fileData[2])) {
		                	String logLine;
		                	if(fileList.length > 1) {
		                		logLine = help.getArtifactId() + ":" + fileList[1].getName() + ":" + help.getFileName();
		                	}
		                	else {
		                		logLine = help.getArtifactId() + ":" + fileList[0].getName() + ":" + help.getFileName();
		                	}
						logList.add(logLine);
		                }
		        }
		            }catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
		Files.write(manifestPath, list, chset, StandardOpenOption.APPEND);
		
		if(command.equals("Create") || command.equals("Checkin")) {
		Files.write(logPath, logList, chset, StandardOpenOption.APPEND);
		}
	}
	
		
	
	/**
	 * This function Creates an "Activity" directory at the path that is specified.
	 * @param pathToRepository - The path to create the activity directory at.
	 */
	public static void CreateActivityDir(String pathToRepository) throws IOException{
		Path path = FileSystems.getDefault().getPath(pathToRepository + FileSystems.getDefault().getSeparator() + "Activity");
		if(!Files.exists(path)){
			Files.createDirectory(path);
		}
	}
	
	/**
	 * Calculates the Artifact ID of a file.
	 * @param file- The file whose artifact id is to be created
	 * @return ArtifactID
	 */
	private static long getArtifactId(File file) {
		long id = 0;
		int ch;
		int weightLen = weightChecksum.length;

		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			for (int i = 0; (ch = bufferedReader.read()) > 0; i++) {
				id += ch * weightChecksum[i % weightLen];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return id;

	}

	/**
     * Appends Artifact ID with file size and file extension and returns as artifact name.
     * @param file - The file whose artifact name is to be returned
     * @return Artifact name
     */
    static String getArtifactName(File file) {
        long id = Main.getArtifactId(file);
        String fileExtension = Main.getFileExtension(file.getName());

        return String.valueOf(id) + "." +
                file.length() + "." +
                fileExtension;
    }
    
    /**
     * Returns file extension
     * @param fileName
     * @return Extension of the file
     */
    private static String getFileExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
        	extension = fileName.substring(i+1);
        }

        return extension;
    }
}

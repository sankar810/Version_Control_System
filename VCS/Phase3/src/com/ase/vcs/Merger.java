package com.ase.vcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Merger {
	
	
public static List<File> getFiles(String destination,List<File> destinationFileList){

		File destDirec = new File(destination);
		File[] listOfFiles = destDirec.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && (!listOfFiles[i].getName().startsWith("Manifest_"))) {
				destinationFileList.add(listOfFiles[i]);
			} else if (listOfFiles[i].isDirectory()) {
				getFiles(listOfFiles[i].getAbsolutePath(),destinationFileList);
			}
		}
		return destinationFileList;
	}
	
	
public static List<String> mergeHandler(String source, String destination) throws IOException{
		
		List<File> destinationFileList= new ArrayList<File>();
		destinationFileList =	Merger.getFiles(destination,destinationFileList);
		
		List<String> destinationFnamesList= new ArrayList<String>();
		List<String> destinationFPathList= new ArrayList<String>();
		for(File f: destinationFileList){
			destinationFnamesList.add(f.getName());
			destinationFPathList.add(f.getAbsolutePath());   	
		}

		List<String> destinationArtifactIdList = new ArrayList<String>();
		for(File f: destinationFileList){
			destinationArtifactIdList.add(ArtGenerator.getArtName(f));
		}

		List<File> sourceFileList= new ArrayList<File>();
		sourceFileList =	Merger.getFiles(source,sourceFileList);
		List<String> sourceFnamesList= new ArrayList<String>();
		List<String> sourceFPathList= new ArrayList<String>();
		for(File f: sourceFileList){
			sourceFnamesList.add(f.getName());
			sourceFPathList.add(f.getAbsolutePath());
		}

		List<String> sourceArtifactIdList = new ArrayList<String>();
		for(File f: sourceFileList){
			sourceArtifactIdList.add(ArtGenerator.getArtName(f));
		}

		List<String> conflictFilesPathList = new ArrayList<String>();
		for(int i=0; i<sourceFnamesList.size();i++){
			if(!(destinationArtifactIdList.contains(sourceArtifactIdList.get(i))) && destinationFnamesList.contains(sourceFnamesList.get(i))){
					
				String conflictFileName= sourceFnamesList.get(i);
				String conflictPath = sourceFPathList.get(i);

				for(int j=0;j<destinationFPathList.size();j++){
					String destinationPath = destinationFPathList.get(j);
					
					if(destinationPath.contains(conflictFileName)){
						conflictFilesPathList.add(destinationPath);
						
						//rename the conflicting file in dest directory with"_MT"
						
						String newPath= destinationPath;
						File f=new File(newPath);
						String ext= ArtGenerator.getFile_Ext(conflictFileName);
						String[] newName= (newPath.split("."+ext));
						File rf1=new File(newName[0].concat("_MT."+ext));
						f.renameTo(rf1);

						//copy conflicting file from src path and rename with "_MR"

						Files.copy(Paths.get(conflictPath), Paths.get(destinationPath), StandardCopyOption.COPY_ATTRIBUTES);
						File rf2=new File(newName[0].concat("_MR."+ext));
						f.renameTo(rf2);

						//find the conflict file from grandparent directory, copy it and append with "_MG"

						String[] destinationManifestFiles = new File(destination + File.separatorChar + "Manifest" + File.separatorChar).list();
						String destinationManifestPath = destination + File.separatorChar + "Manifest" + File.separatorChar + destinationManifestFiles[0];            	
						String ManifestFileSources;
						try(BufferedReader bufferedReader = new BufferedReader(new FileReader(destinationManifestPath))) {
							bufferedReader.readLine();
							bufferedReader.readLine();
							bufferedReader.readLine();
							bufferedReader.readLine();
							ManifestFileSources = bufferedReader.readLine().split(" ")[1];
						}

						String[] parentManifestFilelist = new File(ManifestFileSources + File.separatorChar + "Manifest" + File.separatorChar).list();
						String parentManifestFile = ManifestFileSources + File.separatorChar + "Manifest" + File.separatorChar+parentManifestFilelist[0];
						try(BufferedReader bufferedReader2 = new BufferedReader(new FileReader(parentManifestFile))) {
							for(int k=0;k<8;k++){
								bufferedReader2.readLine();
							}
							Path parentPath=Paths.get("");
							while (bufferedReader2.ready()) {
								String[] entry = bufferedReader2.readLine().split("\t");
								if(conflictFileName.equalsIgnoreCase(entry[1])){
									parentPath = Paths.get(ManifestFileSources + File.separatorChar + entry[2]);
									Files.copy(Paths.get(parentPath+""), Paths.get(destinationPath), StandardCopyOption.COPY_ATTRIBUTES);
									File rf3=new File(newName[0].concat("_MG."+ext));
									f.renameTo(rf3);
								}			
							}							
						}											
					  break;
					}
				}	
			}			
		}//for ends here
		return conflictFilesPathList;		
	}	

}

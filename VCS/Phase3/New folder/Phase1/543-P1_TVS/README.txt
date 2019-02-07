Name: Sankaran Shanmugavelayudam, Tejas Tundulwar, Priya Veerapaneni

Project Name: SCM

Team Initials: TVS

Contact Info: sankaran22.5@gmail.com, ttejas@rocketmail.com, priya.veerapaneni3@gmail.com

Section: CECS 543-01

Class number: 8612

Project part: Part 1

Intro: This project is to form a development team and to build, in Java, the first part of our VCS (Version
Control System) project (AKA an SCM: Source Code Management system). This first part only
implements an initial use-case: Create Repo (Repository). It also makes a number of simplifying
assumptions in order to get to working S/W quickly.
For background material, review on-line user documentation for Fossil, Git, and/or Subversion.
Note, in the terminology of a VCS, an “artifact” is a particular version of a file; if you modify a file, we
call the modified file a new artifact.
The VCS repository holds copies of all versions (i.e., all artifacts) of each file of a project “under
configuration control”. A file name alone is not sufficient to distinguish between several of its
versions/artifacts; hence, within the VCS repository we will use a code name for each artifact and will
put all the artifacts of a particular file in a folder which is named using the original file's name.

External Requirements: Java version 1.8 is required for the SCM project to run.

Build, Installation and Setup: 
	Refer the following to build and run the project:
	1. Open a command prompt and change directory to src\
	2. Type the following command to build all the java files:
		javac com\ase\vcs\*.java 
	3. Type the following command to run the project:
		java com.ase.vcs.Main

Usage:
From the 543-p1_TVS\src directory:
For build: javac com\ase\vcs\*.java
To Run: java com.ase.vcs.Main

Sample invocation and results:
java com.ase.vcs.Main
Usage:

Please enter source and destination path
Source path:
I:\test
Destination path:
I:\testresult
Repository Created Successfully at : I:\testresult

Files List:
Main.java
ArtGenerator.java
PTreeWalker.java
Log.java
LoggingRegister.java


Extra Features: None

Bugs: None detected
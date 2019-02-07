Name: Sankaran Shanmugavelayudam, Tejas Tundulwar, Priya Veerapaneni

Project Name: SCM

Team Initials: TVS

Contact Info: sankaran22.5@gmail.com, ttejas@rocketmail.com, priya.veerapaneni3@gmail.com

Section: CECS 543-01

Class number: 8612

Project part: Part 2

Intro: This is the second part of our SCM (Source Code Management) project. In this project part, we add three new features: check-out, check-in (mostly already done), and labeling. The labeling feature allows the user to add a label (a text string) to a manifest file, in order to make it easier to remember. A manifest file must support up to 4 different labels at the same time. We can presume that the user is nice and always supplies a unique label – so we don't have to check for the label already existing in some other manifest file. A label is supposed to uniquely ID a manifest. The check-out ability lets a user recreate a specific version of the project tree. They do this by selecting a particular manifest file from the repo. The manifest specifies every version of every file required. The recreated project tree is installed in an empty directory, which the user also selects. The repo gets a new manifest file of the checked out version (for its records). The user should be able to specify the manifest file using a label. The check-in ability lets the user update the repository (repo) with changed files in the project tree. Each check-in is a (potentially) different "version" of the project tree, and gets an associated manifest file created for it. This allows the user to track modification history from a given project tree back, through various project versions, all the way to the repo's creation. Labels are forever, so we assume the user doesn't change his/her mind later.

External Requirements: Java version 1.8 is required for the SCM project to run.

Build, Installation and Setup: 
	Refer the following to build and run the project:
	1. Open a command prompt and change directory to src\
	2. Type the following command to build all the java files:
		javac com\ase\vcs\*.java 
	3. Type the following command to run the project:
		java com.ase.vcs.Main

Usage:
From the 543-p2_TVS\src directory:
For build: javac com\ase\vcs\*.java
To Run: java com.ase.vcs.Main

Sample invocation and results:
PS C:\Users\sanka\Desktop\543-P2_TVS\src> java com.ase.vcs.Main
Usage:
Please enter source and destination path

Source path:
I:\New
Destination path:
I:\New1

 Select command :
 1. Create
 2. Check-In
 3. Check-Out
1

 Repository Created Successfully at : I:\New1

PS C:\Users\sanka\Desktop\543-P2_TVS\src> java com.ase.vcs.Main
Usage:
Please enter source and destination path

Source path:
I:\New1
Destination path:
I:\New2

 Select command :
 1. Create
 2. Check-In
 3. Check-Out
3
Select the manifest to checkout: 1
1.      Manifest 2018-03-17 - 23.11.47 Hrs.txt
1

 Repository Checked-Out Successfully at : I:\New2

PS C:\Users\sanka\Desktop\543-P2_TVS\src> java com.ase.vcs.Main
Usage:
Please enter source and destination path

Source path:
I:\New2
Destination path:
I:\New1

 Select command :
 1. Create
 2. Check-In
 3. Check-Out
2
Do you want to Enter Label:
 1. Yes
 2. No
2

 Repository Checked-In Successfully at : I:\New1

Files List:
Main.java
ArtGenerator.java
PTreeWalker.java
Log.java
LoggingRegister.java


Extra Features: None

Bugs: None detected
UML To Java for JPA 
**********
Copyright © 2013
Tom Wallroth,
Philipp Liepert,
Benjamin Granzow

Copyright reserved!

Table of contents
*****************

1 Dependencies
1.1 Linux/Mac
1.2 Windows
1.3 Check installation
2 How to use
3 Locations

1 Dependencies
**************

The project use Gradle to build and install all additional dependencies.
Gradle requires a Java JDK 1.5 or higher to be installed.

Download zip file from http://www.gradle.org/downloads.html and unzip to folder

1.1 Linux/Mac
*************
Extract the distribution archive, i.e. gradle-x.x-bin.tar.gz to the directory you wish to install Gradle.
These instructions assume you chose /usr/local/gradle. The subdirectory gradle-x.x will be created from the archive.
In a command terminal, add Gradle to your PATH variable: export PATH=/usr/local/gradle/gradle-x.x/bin:$PATH
Make sure that JAVA_HOME is set to the location of your JDK, e.g. export JAVA_HOME=/usr/java/jdk1.7.0_06 and that $JAVA_HOME/bin is in your PATH environment variable.

1.2 Windows
***********
Unzip the Gradle download to the folder to which you would like to install Gradle, eg. “C:\Program Files”. The subdirectory gradle-x.x will be created from the archive, where x.x is the version.
Add location of your Gradle “bin” folder to your path. Open the system properties (WinKey + Pause), select the “Advanced” tab, and the “Environment Variables” button, then add “C:\Program Files\gradle-x.x\bin” (or wherever you unzipped Gradle) to the end of your “Path” variable under System Properties. Be sure to omit any quotation marks around the path even if it contains spaces. Also make sure you separated from previous PATH entries with a semicolon “;”.
In the same dialog, make sure that JAVA_HOME exists in your user variables or in the system variables and it is set to the location of your JDK, e.g. C:\Program Files\Java\jdk1.7.0_06 and that %JAVA_HOME%\bin is in your Path environment variable.

1.3 Check installation
**********************
Run gradle –version to verify that it is correctly installed. For windows users use command prompt (type cmd in Start menu).
It should be displayed a simular Output like:

$ gradle -version
------------------------------------------------------------
Gradle 1.0-milestone-3
------------------------------------------------------------

Gradle build time: Monday, 25 April 2011 5:40:11 PM EST
Groovy: 1.7.10
Ant: Apache Ant(TM) version 1.8.2 compiled on December 20 2010
Ivy: 2.2.0
JVM: 1.6.0_20 (Sun Microsystems Inc. 19.0-b09)
OS: Linux 2.6.35-30-generic-pae i386

The remaining dependencies are installed bye Gradle automaticly at the building process.

2 How to use
************

The projectfolder contains two other folders. The JPA-example-UML folder contains the UML files (Model & Profile).
The generator ist in the Semester-Projekt-Workspace folder located.
To create own UML-Models use Eclipse Acceleo and link the folder into the project workspace. Or use the Import function (Type File System) to import the folder. It's recomandet to check the "Create links in workspace" entry unter Advanced options.
Don't forget to apply the UML-Profile in own UML-Models. The JPA annotations are implementet bye stereotypes.

The limitation of the UML-Model are listed in the documentation.

To build the model navigate to the generator <projectPath>/Semester-Projekt-Workspace/pyuml and start generation by rund gradle with $ gradle build .
The java classes and tests are built and the rests run automaticly.

3 Locations
***********
compiled java-classes               <projectPath>/Semester-Projekt-Workspace/pyuml/build/classes/main
compiled test-classes               <projectPath>/Semester-Projekt-Workspace/pyuml/build/classes/test/unittests

gen. java-files                     <projectPath>/Semester-Projekt-Workspace/pyuml/src/main
gen. test-files(TestNG)             <projectPath>/Semester-Projekt-Workspace/pyuml/src/test/unittests
gen. persistence.xml                <projectPath>/Semester-Projekt-Workspace/pyuml/src/ressources/META-INF
Testreports                         <projectPath>/Semester-Projekt-Workspace/pyuml/build/reports/tests

UML-Model & Profile                 <projectPath>/JPA-example-UML

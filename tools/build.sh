#!/bin/sh

mkdir -p ~/Desktop/project-tool/
mkdir ~/Desktop/runnable
cp -r --parents * ~/Desktop/project-tool
echo "Project copied to Desktop/project-tool \n"	

cd ~/Desktop/project-tool/src

javac -classpath lib/.:json-20090211.jar model/*.java
javac -classpath lib/.:json-20090211.jar controller/*.java
# javac -classpath lib/.:json-20090211.jar OTHER_PACKAGES/*.java
echo "Compiled source code\n"


echo "Manifest-Version: 1.0 " > Manifest.txt
echo "Main-Class: controller.Main" >> Manifest.txt
echo "Class-Path: ." >> Manifest.txt

echo "Created Manifest file\n"

jar cfm ~/Desktop/project-tool/runnable/executable.jar Manifest.txt model/*.class controller/*.class 
# Monkey patch for adding json to exacutable 
jar uf ~/Desktop/project-tool/runnable/executable.jar ../lib/org/

cd ~/Desktop/project-tool

echo "Successfully created exacutable jar file, runnable/exacutable.jar"
echo "\n Script done, exiting"

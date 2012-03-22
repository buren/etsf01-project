## Project for ETSF01

************************************
**FOLDERS**
************************************

files: 
	database files, input files and output files
lib: 
	JSON jar file
tools: 
	simple shell script for exporting the project to a jar-file


************************************
**PACKAGES**
************************************

model: 
	JSONDatabase: Reads input from 'files/databaseIN.txt' and adds it to a JSONObject
	

************************************
**EXAMPLE USE OF JSON**
************************************

```java
// Retrieves the entire project list
JSONObject json = JSONDatabase.getInstance();
// Retrieve a single project
JSONObject singleProjectJson = json.get(int index); 
// Get the value correspnding for the specified column 
singleProjectJson.get("column name");
```

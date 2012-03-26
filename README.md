## Project for ETSF01

************************************
**FOLDERS**
************************************

_files:_ 
	database files, input files and output files
_lib:_ 
	JSON jar file
_tools:_ 
	simple shell script for exporting the project to a jar-file


************************************
**PACKAGES**
************************************

**model:**
	_JSONDatabase:_ Reads input from 'files/databaseIN.txt' and adds it to a JSONObject
	
**controller:** 
	_EffortEstimation:_ This is where the similarity function and time estimation is done. 

**view:**
	_GUI:_ The visual assets. 
	
**conversion:**
	_Converter:_ Converts between units person-, -hours, -days, -months, -years

**tests:** 
 	Package with all tests.
 	

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

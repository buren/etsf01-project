package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.*;

public class JSONDatabase 
{

	
	/*********************************************
	 * private constants
	 *********************************************/
	private static final String DATABASE_INPUT_PATH_FIRST = "files/databaseINalt1.txt";
	private static final String DATABASE_INPUT_PATH_SECOND = "files/databaseINalt2.txt";
//	private static final String DATABASE_INPUT_PATH_THIRD = "files/databaseINalt3.txt";
	private static final int PATH_FIRST = 1;
	private static final int PATH_SECOND = 2;
	private static final int PATH_THIRD = 3;
	private static final String DATABASE_OUTPUT_PATH = "files/databaseOUT.txt";
	private static final String DELIMITER = ",";
	private static final int OFFSET_DATABASE_SECOND = 7;
	private static final String[] TYPES = { "RELY", "DATA", "CPLX", "TIME",
			"STOR", "VIRT", "TURN", "ACAP", "AEXP", "PCAP", "VEXP", "LEXP",
			"MODP", "TOOL", "SCED", "Size[kloc]", "Effort[pm]", "Project" };

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	private JSONObject jsonObject;
	private Integer index;
	ArrayList<HashMap<String, String>> rowList;
		
	/*********************************************
	 * Constructors
	 *********************************************/
	
	/**
	 *  Private constructor prevents instantiation from other classes.
	 *  Reads the database files specified in the DATABASE_INPUT_PATHs above
	 *  and instantiates a new JSONObject from the content. All strings are
	 *  converted to lower case.
	 *  
	 */
    private JSONDatabase() {
    	index = 0;
    	jsonObject = new JSONObject();
    	readAndAddFilesToJSON(DATABASE_INPUT_PATH_FIRST);
    	readAndAddFilesToJSON(DATABASE_INPUT_PATH_SECOND);
//    	readAndAddFilesToJSON(DATABASE_INPUT_PATH_THIRD);
    }
    
    
    
    private void readAndAddFilesToJSON(String inputPath){
    	rowList = new ArrayList<HashMap<String, String>>();

    	try {
			BufferedReader reader = new BufferedReader(new FileReader(inputPath));
			
			// HashMap<String, String> where key is the type and value is the column data
			// Reads the first line that contains the types of every column
			String line = reader.readLine().toLowerCase().trim();
			while(line!=null)
			{
				while (line.equals("") || line.charAt(0)== '%' || line.charAt(0)== '@') line = reader.readLine().toLowerCase().trim();
				String[] lineAttributes = line.toLowerCase().split(DELIMITER);
				HashMap<String, String> projectMap;
				// Depending on which file is read, set the appropriate attributes to projectMap
				if (inputPath.equals(DATABASE_INPUT_PATH_FIRST)){
					projectMap = addAttributesFromFile(lineAttributes, PATH_FIRST);
				}else if (inputPath.equals(DATABASE_INPUT_PATH_SECOND)){
					projectMap = addAttributesFromFile(lineAttributes, PATH_SECOND);
				}else{
					projectMap = addAttributesFromFile(lineAttributes, PATH_THIRD);
				}
				rowList.add(projectMap);
				// Adds the HashMap to ArrayList rowList
				String str = index.toString();
				// Adds projectMap to JSON database
				try {
					// Iterates though the list of projects and adds them to JSONObject
					Iterator<HashMap<String, String>> itr = rowList.iterator();
					while (itr.hasNext()) jsonObject.put(str, itr.next());
				} catch (JSONException e) {
					e.printStackTrace();
				} 
				line = reader.readLine();
				if (line == null) break;
				else line.toLowerCase();
				index += 1;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Cannot read " + inputPath  + " file");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cannot read " + inputPath  + " file");
			System.exit(1);
		}
    	// Increment index once more so that index is incremented
    	// inbetween loading of different files
    	index += 1;
    }

    /**
     * Sets the correct values for each column depending on which type of file that is read
     * @param types - list of the types that is used
     * @param lineAttributes - string array with all the values for each column
     * @param fileType - which type of input file is it?
     * @return a hashmap containg all the data. 
     */
	private HashMap<String, String> addAttributesFromFile(String[] lineAttributes, int fileType) {
		// Every element in rowMap is the corresponding data for one single line
		switch (fileType) {
		case PATH_FIRST:
			HashMap<String, String> projectMapFirst = new HashMap<String, String>(); 
			for (int i = 0; i < 17; i++){
				// Puts type as key and its corresponding value for that columns
				// 0..16 is the number of columns to be added
				String attr = lineAttributes[i].trim();
				if (attr.equalsIgnoreCase("very_low"))
					attr = "0";
				else if (attr.equalsIgnoreCase("low"))
					attr = "1";
				else if (attr.equalsIgnoreCase("nominal"))
					attr = "2";
				else if (attr.equalsIgnoreCase("high"))
					attr = "3";
				else if (attr.equalsIgnoreCase("very_high"))
					attr = "4";
				else if (attr.equalsIgnoreCase("extra_high"))
					attr = "5";
				projectMapFirst.put(TYPES[i].toLowerCase(), attr);
			}
			return projectMapFirst;
		case PATH_SECOND:
			HashMap<String, String> projectMapSecond = new HashMap<String, String>();
			for (int i = 0; i < 17; i++){
				// Puts type as key and its corresponding value for that columns
				// 0..16 is the number of columns to be added
				String attr = lineAttributes[i+OFFSET_DATABASE_SECOND].trim();
				if (attr.equalsIgnoreCase("vl"))
					attr = "0";
				else if (attr.equalsIgnoreCase("l"))
					attr = "1";
				else if (attr.equalsIgnoreCase("n"))
					attr = "2";
				else if (attr.equalsIgnoreCase("h"))
					attr = "3";
				else if (attr.equalsIgnoreCase("vh"))
					attr = "4";
				else if (attr.equalsIgnoreCase("xh"))
					attr = "5";
				projectMapSecond.put(TYPES[i].toLowerCase(), attr);
			}
			return projectMapSecond;
		case PATH_THIRD:
			HashMap<String, String> projectMapThird = new HashMap<String, String>(); 
			// TODO: Implement read for 'files/databaseInalt2.txt'
			return projectMapThird;

		default:
			break;
		}
		return null;
	}
    
	/**
	 * Returns the number of projects added to the database
	 * @return number of projects in database
	 */
	public Integer getTotalNumberOfProjects(){
		return index;
	}
	
    /**
     * Singleton getter method
     * @return		a singleton instance of JSONDatase
     */
    public static JSONDatabase getInstance() 
    {
        return SingletonHolder.instance;
    }

    /**
    * SingletonHolder is loaded on the first execution of Singleton.getInstance() 
    * or the first access to SingletonHolder.INSTANCE, not before.
    */
    private static class SingletonHolder 
    { 
    	public static final JSONDatabase instance = new JSONDatabase();
    }
    
    /*********************************************
	 * Getters
	 *********************************************/
   
	/**
	 * Returns the entire specified project and returns it as a {@link JSONObject}.
	 * @param key - key for the project
	 * @return - the entire project as a {@link JSONObject}
	 */
	public JSONObject getProjectAsJSONObject(String key) {
		if (jsonObject.has(key)) {
			try {
				return jsonObject.getJSONObject(key);
			} catch (JSONException e) {
				System.out.println("Key not found!");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	  /**
     * Returns the object associated with the key. 
     * @param key	identifier key 
     * @return 		value of object associated with key
     */
    public Object getJSONValueForKey(String key)
    {
    	if(jsonObject.has(key))
    	{
			try {
				return jsonObject.get(key);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	return null;
    }


    /***********************************************************
     * Misc. methods
     **********************************************************/
    

    /**
     * Writes the JSONDatabase to file <i>{@value DATABASE_OUTPUT_PATH}</i>. 
     */
    public void writeJSONtoFile() {
    	FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(DATABASE_OUTPUT_PATH);
	    	BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	    	bufferedWriter.write(jsonObject.toString());
	    	bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /** 
    * Returns the entire JSONObject as a string.
    * @return the jsonObject as a string
    */
    public String print() {
    	try {
			return jsonObject.toString(3);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }



	public JSONObject getJSONObject() {
		return jsonObject;
	}
}
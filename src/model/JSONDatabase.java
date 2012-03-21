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
	private static final String DATABASE_INPUT_PATH = "files/databaseIN.txt";
	private static final String DATABASE_OUTPUT_PATH = "files/databaseOUT.txt";
	private JSONObject jsonObject;
	private static final String DELIMITER = ",";
	
		
	/*********************************************
	 * Constructors
	 *********************************************/
	
	/**
	 *  Private constructor prevents instantiation from other classes
	 *  Reads the database file specified in the DATABASE_INPUT_PATH above
	 *  and instantiates a new JSONObject from its content
	 */
    private JSONDatabase() 
    {

		ArrayList<HashMap<String, String>> rowList = new ArrayList<HashMap<String, String>>();
		jsonObject = new JSONObject();
    	try {
			BufferedReader reader = new BufferedReader(new FileReader(DATABASE_INPUT_PATH));
			
			// HashMap<String, String> where key is the type and value is the column data
			// Reads the first line that contains the types of every column
			String[] types = reader.readLine().split(DELIMITER);
			String line = reader.readLine();
			Integer index = 0;
			while(line!=null)
			{
				String[] lineAttributes = line.split(DELIMITER);
				// Every element in rowMap is the corresponding data for one single line
				HashMap<String, String> projectMap = new HashMap<String, String>(); 
				for (int i = 0; i < 17; i++){
					// Puts type as key and its corresponding value for that columns
					// 0..16 is the number of columns to be added
					projectMap.put(types[i], lineAttributes[i]);
					rowList.add(projectMap);
				}
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
				index += 1;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Cannot read files/database.txt file");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cannot read files/database.txt file");
			System.exit(1);
		}
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
	 * Returns entry in JSON databse from key. Ignores case.
	 * @param key
	 * @return
	 */
	public JSONObject getEntry(String key) {
		JSONObject entry = null;
		entry = (JSONObject) getJSONValueForKey(key);
		if (entry == null && key.split(" ").length > 1) {
			String[] nameArr = key.split(" ");
			key = nameArr[0].substring(0, 1).toUpperCase() + nameArr[0].substring(1) + " "
				+ nameArr[1].substring(0, 1).toUpperCase() + nameArr[1].substring(1);
			entry = (JSONObject) getJSONValueForKey(key);
		}
		if (entry == null) {
			key = key.toUpperCase();
			entry = (JSONObject) getJSONValueForKey(key);
		}
		return entry;
	}
	
	
	public JSONObject get(String key){
		try {
			return jsonObject.getJSONObject(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 
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
     * Puts value in the database with the key key. For testing purposes only.
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
    	try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Replaces the entry for row key with the String 
     * @param key Name of the patient
     * @param value The value
     * @throws JSONException if key is not in database
     */
    public void writeDatabase(String key, String value) throws JSONException {
    	JSONObject entry = null;
    	try {
    		entry = (JSONObject) jsonObject.get(key);
    	} catch (JSONException e) {
    		String[] nameArr = key.split(" ");
			key = nameArr[0].substring(0, 1).toUpperCase() + nameArr[0].substring(1) + " "
				+ nameArr[1].substring(0, 1).toUpperCase() + nameArr[1].substring(1);
			entry = (JSONObject) getJSONValueForKey(key);
			
    	}
    	entry.put("Row", value);
    	writeJSONtoFile();
    }

    /**
    * Creates a new row 
	* @param projectName Name of the project
	* @param projectAttributes a HashMap<String, String> containing the projectAttributes to be written
	*/
    public void createNewProject(String projectName, HashMap<String, String> projectAttributes){
    	try {
			jsonObject.accumulate(projectName, projectAttributes);
			writeJSONtoFile();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
  
    /**
     * Removes the entry called key from the database.
     * @param key
     * @return true if entry was removed, false if it does not exist
     */
    public boolean deleteEntry(String key) {
    	JSONObject entry = null;
		entry = (JSONObject) getJSONValueForKey(key);
		if (entry == null && key.split(" ").length > 1) {
			String[] nameArr = key.split(" ");
			key = nameArr[0].substring(0, 1).toUpperCase() + nameArr[0].substring(1) + " "
				+ nameArr[1].substring(0, 1).toUpperCase() + nameArr[1].substring(1);
			entry = (JSONObject) getJSONValueForKey(key);
		}
		if (entry == null) {
			key = key.toUpperCase();
			entry = (JSONObject) getJSONValueForKey(key);
		}
    	entry = (JSONObject) jsonObject.remove(key);
    	writeJSONtoFile();
    	return entry != null;
    }
    

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
    * Prints the json object
    */
    public String print() {
    	return jsonObject.toString();
    }
}
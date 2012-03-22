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
	 *  and instantiates a new JSONObject from its content all strings are
	 *  converted to lower case.
	 *  
	 */
    private JSONDatabase() 
    {

		ArrayList<HashMap<String, String>> rowList = new ArrayList<HashMap<String, String>>();
		jsonObject = new JSONObject();
    	try {
			BufferedReader reader = new BufferedReader(new FileReader(DATABASE_INPUT_PATH));
			
			// HashMap<String, String> where key is the type and value is the column data
			// Reads the first line that contains the types of every column
			String[] types = reader.readLine().toLowerCase().split(DELIMITER);
			String line = reader.readLine().toLowerCase();
			Integer index = 0;
			while(line!=null)
			{
				String[] lineAttributes = line.toLowerCase().split(DELIMITER);
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
				if (line == null) break;
				else line.toLowerCase();
				index += 1;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Cannot read " + DATABASE_INPUT_PATH  + " file");
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Cannot read " + DATABASE_INPUT_PATH  + " file");
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
    	return jsonObject.toString();
    }
}
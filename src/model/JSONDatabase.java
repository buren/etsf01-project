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

import conversion.Converter;

public class JSONDatabase 
{

	
	/*********************************************
	 * public constants
	 *********************************************/
	public static final String[] TYPES = { "RELY", "DATA", "CPLX", "TIME",
		"STOR", "VIRT", "TURN", "ACAP", "AEXP", "PCAP", "VEXP", "LEXP",
		"MODP", "TOOL", "SCED", "Size[kloc]", "Effort[pm]", "Project", "RELY", "DATA", "CPLX", "TIME",
		"STOR", "VIRT", "TURN", "ACAP", "AEXP", "PCAP", "VEXP", "LEXP",
		"MODP", "TOOL", "SCED", "Size[kloc]", "Effort[pm]", "Project", "RELY", "DATA", "CPLX", "TIME",
		"STOR", "VIRT", "TURN", "ACAP", "AEXP", "PCAP", "VEXP", "LEXP",
		"MODP", "TOOL", "SCED", "Size[kloc]", "Effort[pm]", "Project", "RELY", "DATA", "CPLX", "TIME",
		"STOR", "VIRT", "TURN", "ACAP", "AEXP", "PCAP", "VEXP", "LEXP",
		"MODP", "TOOL", "SCED", "Size[kloc]", "Effort[pm]", "Project" };
	
	/*********************************************
	 * private constants and deafult values
	 *********************************************/
	private static final String DATABASE_INPUT_PATH_FIRST = "files/databaseINalt1.txt";
	private static final String DATABASE_INPUT_PATH_SECOND = "files/databaseINalt2.txt";
	private static final String DATABASE_INPUT_PATH_THIRD = "files/databaseINalt3.txt";
	private static final String DATABASE_OUTPUT_PATH = "files/databaseOUT.txt";
	private static final String DEFAULT_DELIMITER = ",";
	private static final String DEFAULT_IGNORE_PATTERNS = "@%";
	private static final String DEFAULT_COLUMNS_FIRST = "0-16";
	private static final String DEFAULT_COLUMNS_SECOND = "7-23";
	private static final String DEFAULT_COLUMNS_THIRD = "0-14";
	private static final int OFFSET_DATABASE_SECOND = 7;
	private static final int PATH_FIRST = 1;
	private static final int PATH_SECOND = 2;
	private static final int PATH_THIRD = 3;
	
	private static final String[] VALUE_NAMES_FIRST = {"very_low", "low", "nominal",  "high", "very_high", "extra_high"};
	private static final String[] VALUE_NAMES_SECOND = {"vl", "l", "n", "h", "vh", "xh"};
	private static final String[] VALUE_NAMES_THIRD = {};
	
	

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	private JSONObject jsonObject;
	private Integer index;
	private ArrayList<HashMap<String, String>> rowList;
	
		
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
    	//TODO: Find a pretty way to append data to the jsonObjects
    	FileHandler fileHandler = new FileHandler(DATABASE_INPUT_PATH_FIRST, DEFAULT_DELIMITER, DEFAULT_IGNORE_PATTERNS, DEFAULT_COLUMNS_FIRST, VALUE_NAMES_FIRST, Converter.MONTHS);
		String db = "";
		try {
			db = fileHandler.readDatabase().toString(2);
			System.out.println("length is " + db.length());
			fileHandler = new FileHandler(DATABASE_INPUT_PATH_SECOND, DEFAULT_DELIMITER, DEFAULT_IGNORE_PATTERNS, DEFAULT_COLUMNS_SECOND, VALUE_NAMES_SECOND, Converter.MONTHS);
			db += fileHandler.readDatabase().toString(2);	
			System.out.println("length is " + db.length());
			fileHandler = new FileHandler(DATABASE_INPUT_PATH_THIRD, DEFAULT_DELIMITER, DEFAULT_IGNORE_PATTERNS, DEFAULT_COLUMNS_THIRD, VALUE_NAMES_THIRD, Converter.MONTHS);
			db += fileHandler.readDatabase().toString(2);
			System.out.println("length is " + db.length());
			JSONTokener tokener = new JSONTokener(db);
			jsonObject = new JSONObject(tokener);
		}catch(JSONException e){
			System.exit(1);
		}
    }
    
    /**
     * Reads the user defined database file and adds it to the database. 
     * @param inputPath path to the database.
     * @param delimiter the delimiter to be used. 
     * @param ignorePattern lines beginning with this pattern will be ignored. 
     * @param numberOfAttributes which columns will be included. 
     */
    public void addDatabase(String inputPath, String delimiter, String ignorePattern, String includedColumns, String[] valueNames, int timeUnit){
    	FileHandler fileHandler = new FileHandler(inputPath, delimiter, ignorePattern, includedColumns, valueNames, timeUnit);
    	// Reads the database
    	//TODO: Find a pretty way to append data to the jsonObject
    	try {
			String db = jsonObject.toString() + fileHandler.readDatabase().toString(0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public JSONObject getOneProjectAsJSONObject(String key) {
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



	public JSONObject getDatabaseAsJSONObject() {
		return jsonObject;
	}
}
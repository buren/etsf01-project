package tests;

import junit.framework.TestCase;

import model.FileHandler;
import model.JSONDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class TestFileHandler extends TestCase {
	
	private static final String EFFORTPM_COLUMN = "effort[pm]";
	private static final String CPLX_COLUMN = "cplx";
	private static final String RELY_COLUMN = "rely";
	// First line values
	private static final String FIRST_LINE_RELY_VALUE = "2";
	private static final String FIRST_LINE_CPLX_VALUE = "4";
	private static final String FIRST_LINE_EFFORT_VALUE = "42256.0";

	// Last line values
	private static final String LAST_LINE_RELY_VALUE = "3";
	private static final String LAST_LINE_CPLX_VALUE = "4";
	private static final String LAST_LINE_EFFORT_VALUE = "5776.0";

	// Index for first and last lines
	private static final String FIRST_LINE_INDEX = "0";
	private static final String LAST_LINE_INDEX = "152";
	
	
	
	private FileHandler fileHandler;private static final String[] VALUE_NAMES_FIRST = {"very_low", "low", "nominal",  "high", "very_high", "extra_high"};
	private static final String[] VALUE_NAMES_SECOND = {"vl", "l", "n", "h", "vh", "xh"};
	

	private JSONDatabase database;
	private JSONObject jsonObject;
	
	
	@Before
	public void setUp(){
		 database = JSONDatabase.getInstance();
	}
	
	
	@Test
	public void testReadFirstDatabase(){
		fileHandler = new FileHandler();
			
	//	try {
			jsonObject = fileHandler.readDatabase("files/databaseINalt1.txt", ",", "%@", "0-16", VALUE_NAMES_FIRST , 2);
			System.out.println(jsonObject.toString());
			// TODO: Implemented complete test
			// 		now its impossible to test since every projects
			// 		is mapped to a UUID
			/*
			assertEquals(FIRST_LINE_RELY_VALUE , ((JSONObject) jsonObject.get(FIRST_LINE_INDEX)).get(RELY_COLUMN));
			assertEquals(FIRST_LINE_CPLX_VALUE , ((JSONObject) jsonObject.get(FIRST_LINE_INDEX)).get(CPLX_COLUMN));
			assertEquals(FIRST_LINE_EFFORT_VALUE, ((JSONObject) jsonObject.get(FIRST_LINE_INDEX)).get(EFFORTPM_COLUMN));
			// Test last project
			assertEquals(LAST_LINE_RELY_VALUE , ((JSONObject) jsonObject.get(LAST_LINE_INDEX)).get(RELY_COLUMN));
			assertEquals(LAST_LINE_CPLX_VALUE , ((JSONObject) jsonObject.get(LAST_LINE_INDEX)).get(CPLX_COLUMN));
			assertEquals(LAST_LINE_EFFORT_VALUE, ((JSONObject) jsonObject.get(LAST_LINE_INDEX)).get(EFFORTPM_COLUMN));
			*/ 
		//} 
		//catch (JSONException e) {
		//	e.printStackTrace();
		//}	
	}
	
	@Test
	public void testReadSecondDatabase(){
		fileHandler = new FileHandler();
		try {
			fileHandler.readDatabase("files/databaseINalt2.txt", ",", "%@", "7-23", VALUE_NAMES_SECOND , 2).toString(2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testReadThirdDatabase(){
		fileHandler = new FileHandler();
		try {
			System.out.println(fileHandler.readDatabase("files/databaseINalt3.txt", ",", "%@", "0-14", VALUE_NAMES_SECOND , 2).toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


}

package tests;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import model.JSONDatabase;

public class TestJSONDatabase extends TestCase{

	/*********************************************
	 *  PRIVATE STATIC CONSTANTS
	 *********************************************/
	
	private static final String TEST_STRING_RELY_FOR_FIRST_LINE = "Nominal";
	private static final String TEST_STRING_RELT_FOR_LAST_LINE = "Nominal";
	private static final String TEST_STRING_EFFORT_FOR_FIRST_LINE = "278";
	private static final String TEST_STRING_EFFORT_FOR_LAST_LINE = "155";
	private static final String FIRST_LINE_INDEX = "0";
	private static final String LAST_LINE_INDEX = "59";
	

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	
	private JSONObject firstJSON;
	private JSONObject lastJSON;
	
	@Before
	public void setUp(){
		 firstJSON= JSONDatabase.getInstance().getProjectAsJSONObject(FIRST_LINE_INDEX);
		 lastJSON = JSONDatabase.getInstance().getProjectAsJSONObject(LAST_LINE_INDEX);
	}
	
	@Test
	public void testReadFromDatabaseINFile(){
		try {
			assertEquals("Nominal" , firstJSON.get("RELY"));
			assertEquals("Nominal" , lastJSON.get("RELY"));
			assertEquals("Very_High" , firstJSON.get("CPLX"));
			assertEquals("High" , lastJSON.get("CPLX"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
}

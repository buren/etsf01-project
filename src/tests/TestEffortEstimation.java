package tests;

import static org.junit.Assert.*;

import java.util.Iterator;
import model.JSONDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import controller.EffortEstimation;

public class TestEffortEstimation {

	/*********************************************
	 *  PRIVATE STATIC CONSTANTS
	 *********************************************/
	
	private static final String EFFORTPM_COLUMN = "effort[pm]";
	private static final String CPLX_COLUMN = "cplx";
	private static final String RELY_COLUMN = "rely";
	// First line values
	private static final String FIRST_LINE_RELY_VALUE = "nominal";
	private static final String FIRST_LINE_CPLX_VALUE = "very_high";
	private static final String FIRST_LINE_EFFORT_VALUE = "278";

	// Last line values
	private static final String LAST_LINE_RELY_VALUE = "nominal";
	private static final String LAST_LINE_CPLX_VALUE = "high";
	private static final String LAST_LINE_EFFORT_VALUE = "155";

	// Index for first and last lines
	private static final String FIRST_LINE_INDEX = "0";
	private static final String LAST_LINE_INDEX = "59";
	

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	
	private JSONDatabase database;
	
	@Before
	public void setUp(){
		 database = JSONDatabase.getInstance();
	}
	
	@Test
	public void testSimilarity() throws JSONException{
		JSONObject db = database.getDatabaseAsJSONObject();
		EffortEstimation estimator = new EffortEstimation(database.getDatabaseAsJSONObject());
		JSONObject futureProject = database.getOneProjectAsJSONObject("1");

		JSONObject similarList = estimator.calculateSimilarity(futureProject);
		Iterator iter = similarList.sortedKeys();
		while(iter.hasNext()) {
			String index = (String) iter.next();
			JSONObject project = (JSONObject) similarList.get(index);
			System.out.println("Similarity between future project and project " + index + " is " + project.get("similarity"));
		}
	}
	
	@Test
	public void testDistance(){
		EffortEstimation estimator = new EffortEstimation(database.getDatabaseAsJSONObject());
		assertTrue(estimator.distance(4, 4, 5, 0) == 0);
	}
	
	@Test
	public void testEffortEstimation() {
		EffortEstimation estimator = new EffortEstimation(database.getDatabaseAsJSONObject());
		JSONObject projectList = new JSONObject();
		for (int i = 1; i <= 5; i++) {
			try {
				JSONObject proj = database.getOneProjectAsJSONObject("" + i);
				proj.put("effort[pm]", ""+ (50*i));
				double similarity = 0.1 * i;
				proj.put("similarity", "" + similarity);
				projectList.put("" + i, proj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int correctResult = (int) Math.round((50*0.1+100*0.2+150*0.3+200*0.4+250*0.5) / 5.0); 
		assertEquals(correctResult, estimator.calculateEffortEstimation(projectList));
	}
	
}

package tests;

import static org.junit.Assert.*;

import java.util.Random;

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
	public void testSimilarity() {
		EffortEstimation estimator = new EffortEstimation(database.getJSONObject());
		estimator.calculateSimilarity(database.getProjectAsJSONObject("1"));
	}
	
	@Test
	public void testEffortEstimation() {
		Random rand = new Random();
		EffortEstimation estimator = new EffortEstimation(database.getJSONObject());
		JSONObject projectList = new JSONObject();
		for (int i = 1; i <= 5; i++) {
			try {
				JSONObject proj = database.getProjectAsJSONObject("" + i);
				proj.put("effort[pm]", ""+ (50*i));
				double similarity = 0.1 * i;
				proj.put("Similarity", "" + similarity);
				projectList.put("" + i, proj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertEquals((int) Math.round(50*0.1+100*0.2+150*0.3+200*0.4+250*0.5), estimator.calculateTimeEstimation(projectList));
	}
	
}

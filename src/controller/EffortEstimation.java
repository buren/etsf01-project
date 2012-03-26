package controller;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class EffortEstimation {

	

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	
	private JSONObject database;
	private double threshold;
	

	/*********************************************
	 * CONSTRUCTORS
	 *********************************************/
	
	/**
	 * @param database the database for all done projects read from file. 
	 * @param threshold user defined threshold for how similar the new and 
	 * old projects needs to be. All projects under threshold limit will be used
	 *  when calculating the time estimation.
	 */
	public EffortEstimation(JSONObject database, double threshold){
		this.database = database;
		this.threshold = threshold;
	}
	
	/**
	 * Constructor when user doesn't specify the threshold. Threshold defaults to 0.5
	 * @param database the database for all done projects read from file.
s	 */
	public EffortEstimation(JSONObject database){
		this.database = database;
		this.threshold = 0.5;
	}
	/**
	 * Calculates how similar the new projects is compared to done projects. 
	 * Invokes calculateTimeEstimation() for all projects that are below threshold for similarity.
	 * @param futureProject
	 */
	public void calculateSimilarity(JSONObject futureProject){

	}
	
	/**
	 * Calculates a list of the estimated times. 
	 * Calculates estimated time for all projects thats it similar < threshold
	 * @param listOfSimilarProjects
	 */
	private void calculateTimeEstimation(JSONObject listOfSimilarProjects){
		double est = 0;
		double effort = 0;
		double similarity = 0;
		Iterator it = listOfSimilarProjects.keys();
		while (it.hasNext()) {
			JSONObject proj = (JSONObject) it.next(); 
			try {
				effort = Double.parseDouble(proj.getString("effort[pm]"));
				similarity = Double.parseDouble(proj.getString("Similarity"));
			} catch (NumberFormatException e) {
				System.err.println("EffortEstimation.calculateTimeEstimation: Bad effort value in database");
				e.printStackTrace();
			} catch (JSONException e) {
				System.err.println("EffortEstimation.calculateTimeEstimation: Missing effort value in database");
				e.printStackTrace();
			}
			est += similarity * effort;
		}
		est /= listOfSimilarProjects.length();
	}
}

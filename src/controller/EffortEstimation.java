package controller;

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
	 * Constructor when user doesn't specify the threhold. Threshold defaults to 0.5
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
	}
}

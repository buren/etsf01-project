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
	public EffortEstimation(JSONObject database, double threshold) {
		this.database = database;
		this.threshold = threshold;
	}
	
	/**
	 * Constructor when user doesn't specify the threshold. Threshold defaults to 0.5
	 * @param database the database for all done projects read from file.
s	 */
	public EffortEstimation(JSONObject database) {
		this.database = database;
		this.threshold = 0.5;
	}
	/**
	 * Calculates how similar a new project is compared to the finished projects in the database.
	 * Returns a JSONObject containing the list of projects whose similarity > threshold.
	 * @param futureProject
	 */
	public JSONObject calculateSimilarity(JSONObject futureProject) {
		JSONObject listOfSimilarProjects = new JSONObject();
		Iterator iter = database.keys();
		while (iter.hasNext()) {
			String index = (String) iter.next();
			try {
				listOfSimilarProjects.put(index, database.get(index));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println(index);
		}
		return listOfSimilarProjects;
	}
	
	/**
	 * Calculates the effort, in person-hours, for a project based on a list of similar projects.
	 * @param listOfSimilarProjects
	 */
	private int calculateTimeEstimation(JSONObject listOfSimilarProjects){
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
		return (int) Math.round(est);
	}
}

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
		Iterator iter = database.sortedKeys();
		while (iter.hasNext()) {
			double distanceSum = 0;
			int nbrOfAttributes = 0;
			String index = (String) iter.next();
			try {
				JSONObject project = (JSONObject) database.get(index);
				Iterator projIter = project.sortedKeys();
				while (projIter.hasNext()) {
					String attribute = (String) projIter.next();
					if (!attribute.equals("size[kloc]") && !attribute.equals("effort[pm]")) {
						int futureValue = Integer.parseInt((String) futureProject.get(attribute));
						int oldValue = Integer.parseInt((String) project.get(attribute));
						distanceSum += distance(futureValue, oldValue, 5, 0);
						nbrOfAttributes++;
					}
				}
				double similarity = Math.sqrt(distanceSum/nbrOfAttributes);
				System.out.println("Similarity between future project and project " + index + " is " + similarity);
				listOfSimilarProjects.put(index, database.get(index));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listOfSimilarProjects;
	}
	
	/**
	 * Calculates the Euclidean distance between two attributes given a possible max and min
	 * of those attributes.
	 * @param value1
	 * @param value2
	 * @param max
	 * @param min
	 * @return
	 */
	public double distance(double value1, double value2, double max, double min) {
		return (Math.abs(value1 - value2) / (max - min))
			 * (Math.abs(value1 - value2) / (max - min));
	}
	
	/**
	 * Calculates the effort, in person-hours, for a project based on a list of similar projects.
	 * @param listOfSimilarProjects
	 */
	public int calculateEffortEstimation(JSONObject futureProject) {
		return 0;
	}
}

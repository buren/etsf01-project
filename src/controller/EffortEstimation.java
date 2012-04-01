package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import conversion.Converter;

public class EffortEstimation {

	/*********************************************
	 * PUBLIC CONSTANTS
	 *********************************************/
	public static final String[] TYPES = { "RELY", "DATA", "CPLX", "TIME",
		"STOR", "VIRT", "TURN", "ACAP", "AEXP", "PCAP", "VEXP", "LEXP",
		"MODP", "TOOL", "SCED", "Size[kloc]", "Effort[pm]", "Project" };

	
	/*********************************************
	 * PRIVATE CONSTANTS
	 *********************************************/
	private double SIMILARITY_THRESHOLD = 0.83;
	private static final String FILEPATH_FOR_FUTURE_PROJECT = "files/futureproject.json";

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
	 * @return a list of projects that are similar above the threshold
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
					double maxDistance = maximumDistance(attribute);
					if (!attribute.equals("effort[pm]")) {
						double futureValue = Double.parseDouble((String) futureProject.get(attribute));
						double oldValue = Double.parseDouble((String) project.get(attribute));
						distanceSum += distance(futureValue, oldValue, maxDistance);
						nbrOfAttributes++;
					}
				}
				double similarity = 1 - Math.sqrt(distanceSum/nbrOfAttributes);
				if (similarity > SIMILARITY_THRESHOLD) {
					project.put("similarity", similarity);
					listOfSimilarProjects.put(index, project);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listOfSimilarProjects;
	}
	
	private double maximumDistance(String attribute) {
		if (!attribute.equals("size[kloc]")) {
			return 5;
		}
		double min = Integer.MAX_VALUE;
		double max = 0;
		Iterator iter = database.sortedKeys();
		while (iter.hasNext()) {
			String index = (String) iter.next();
				JSONObject project;
				try {
					project = (JSONObject) database.get(index);
					double value = Double.parseDouble((String) project.get(attribute));
					if (value < min)
						min = value;
					if (value > max )
						max = value;
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return max - min;
	}
	
	/**
	 * Calculates the Euclidean distance between two attributes given a possible max and min
	 * of those attributes.
	 * @param value1
	 * @param value2
	 * @param maxDistance
	 * @param min
	 * @return the euclidean distance 
	 */
	public double distance(double value1, double value2, double maxDistance) {
		return (Math.abs(value1 - value2) / (maxDistance))
			 * (Math.abs(value1 - value2) / (maxDistance));
	}
	
	/**
	 * Calculates the effort, in person-hours, for a project based on a list of similar projects.
	 * @param listOfSimilarProjects
	 */
	public int calculateEffortEstimation(JSONObject listOfSimilarProjects){
		double est = 0;
		double effort = 0;
		double similarity = 0;
		Iterator it = listOfSimilarProjects.keys();
		while (it.hasNext()) { 
			try {
				JSONObject proj = database.getJSONObject((String) it.next());
				effort = Double.parseDouble(proj.getString("effort[pm]"));
				similarity = Double.parseDouble(proj.getString("similarity"));
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
	
	/**
	 * Invoked by GUI. 
	 * @param futureProject - the project to be estimated
	 * @return the time estimation for the project
	 */
	public int calculateEffortForProject(HashMap<String, String> futureProject){
		try {
			System.out.println(new JSONObject(futureProject).toString(2));
		} catch (JSONException e) {e.printStackTrace();}
		return (int) Math.round(Converter.convertToMonths(Converter.HOURS, calculateEffortEstimation(calculateSimilarity(new JSONObject(futureProject)))));
	}
}

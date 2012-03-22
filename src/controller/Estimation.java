package controller;

import org.json.JSONObject;

public class Estimation {

	/*********************************************
	 * PUBLIC CONSTANTS
	 *********************************************/
	public static final int PERSON_HOURS = 1;
	public static final int PERSON_DAYS = 2;
	public static final int PERSON_MONTHS = 3;
	public static final int PERSON_YEARS = 4;


	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	
	private JSONObject database;
	private double threshold;
	

	/*********************************************
	 * CONSTRUCTORS
	 *********************************************/
	public Estimation(JSONObject database, double threshold){
		this.database = database;
		this.threshold = threshold;
	}
	
	public Estimation(JSONObject database){
		this.database = database;
		// Defaults to 0.5
		this.threshold = 0.5;
	}
	
	
}

package controller;

import org.json.JSONObject;

public class Estimation {

	

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

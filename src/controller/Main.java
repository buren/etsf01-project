package controller;


import org.json.JSONException;
import org.json.JSONObject;

import model.JSONDatabase;

public class Main {


	/*********************************************
	 * Main method
	 *********************************************/
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new  Main().run();
	}
	/*********************************************
	 * Public methods
	 *********************************************/
	public void run() {
		JSONObject json = JSONDatabase.getInstance().get("1");
		try {
			System.out.println(json.get("RELY"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(JSONDatabase.getInstance().get("1"));
		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("2").toString());
		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("59").toString());
////		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("DATA"));
//		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("CPLX"));
//		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("TIME"));
//		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("STOR"));


	}
}

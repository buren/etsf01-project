package controller;

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
		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("1").toString());
		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("2").toString());
		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("59").toString());
////		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("DATA"));
//		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("CPLX"));
//		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("TIME"));
//		System.out.println(JSONDatabase.getInstance().getJSONValueForKey("STOR"));


	}
}

package controller;

import view.GUI;

import model.JSONDatabase;

public class Main {


	/*********************************************
	 * Main method
	 *********************************************/
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		run();
	}
	
	/*********************************************
	 * Public methods
	 *********************************************/
	public static void run() {
		JSONDatabase database = JSONDatabase.getInstance();
//		System.out.println(database.print());
		new GUI(database.getDatabaseAsJSONObject());
	}
}

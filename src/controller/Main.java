package controller;

import java.util.ArrayList;
import java.util.Scanner;

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
		JSONDatabase json = JSONDatabase.getInstance();
		System.out.println(json.print());
	}
}

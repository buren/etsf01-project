package view;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.RestoreAction;

import org.json.JSONException;
import org.json.JSONObject;
import org.omg.CORBA.NO_IMPLEMENT;

import controller.EffortEstimation;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GUI implements ActionListener {
	
	
	
	
	/*********************************************
	 * PRIVATE CONSTANTS
	 *********************************************/
	private static final String NO_INPUT = "0";
	private static final int ROWS = 4;
	private static final int COLUMNS = 4;
	private static final char IDENTIFIER = '#';
	private static final String LINE_ENDING = "\r\n";
	private static final String FUTURE_PROJECT_PATH = "files/futureproject.json";

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	private JPanel mainPanel;
	private JTextField matrixBoxValue;
	private JTextField[][] matrixPanel;
	private JButton submitButton;
	private JButton clearButton;
	private JTextField resultField;
	private EffortEstimation effortEstimation;

	/**
	 * Creates the GUI with default input values.
	 */
	public GUI(JSONObject database) {
		
		effortEstimation = new EffortEstimation(database);
		
		JPanel mainPanel2  = new JPanel();
		// Label for the windows
		JFrame frame = new JFrame("Effort Estimation Calculator");
		mainPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
		
		
		// Program will exit when pressing the "x" in the top right corner
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Default size of the window
		frame.setSize(300, 300);
		
		// Inits buttons and result field and adds them to the layout
		submitButton = new JButton("Submit");
		clearButton = new JButton("Clear");
		resultField = new JTextField();
		resultField.setText("Result");
		mainPanel2.add(resultField, BorderLayout.SOUTH);
		mainPanel2.add(submitButton, BorderLayout.SOUTH);
		mainPanel2.add(clearButton, BorderLayout.SOUTH);

		matrixPanel = new JTextField[ROWS][COLUMNS];
		mainPanel2.add(submitButton);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(mainPanel2, BorderLayout.SOUTH);
		submitButton.addActionListener(this);
		
		// Creates the 4x4 view for the GUI 
		int index = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				matrixBoxValue = new JTextField();
				matrixBoxValue.setBackground(Color.LIGHT_GRAY);
				matrixBoxValue.setText( IDENTIFIER + EffortEstimation.TYPES[index++]);
				matrixPanel[r][c] = matrixBoxValue;
				mainPanel.add(matrixBoxValue);
			}
		}
		frame.setVisible(true);
	}
	
	/**
	 * Clear the GUI from all inputs, to default values
	 */
	public void clearGUI() {
		int index = 0;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				matrixPanel[row][col].setText(IDENTIFIER + EffortEstimation.TYPES[index++]);
			}
		}
	}
	/**
	 * Collects the values that are inputed in GUI.
	 * If a field still has its default value, NO_INPUT is added to the the field.
	 */
	public void collectStartValuesFromGUI()  {
		HashMap<String, String> project = new HashMap<String, String>();
		int index = 0;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if(matrixPanel[row][col].getText().charAt(0) == IDENTIFIER)
					project.put(EffortEstimation.TYPES[index++].toLowerCase(), NO_INPUT);
				else
					project.put(EffortEstimation.TYPES[index++].toLowerCase(), matrixPanel[row][col].getText());
			}
		}
		project.put("effort[pm]", NO_INPUT);
		writeToFile(project);
		resultField.setText(String.valueOf(calculate(project)));
	}
	/**
	 * Calculates and returns the value of the estimation.
	 * @param project the future project to estimate
	 * @return the value of the estimation as an int.
	 * 
	 */
	private int calculate(HashMap<String, String> project){
		return effortEstimation.calculateEffortForProject(project);
	}
	
	/**
	 * Writes the project to json formatted file (files/futureproject.json).
	 * @param project the project to be written to json formatted file. 
	 */
	private void writeToFile(HashMap<String, String> project){
		JSONObject json = new JSONObject(project);
		try {
			String jsonAsString = json.toString(2);
			FileWriter fileWriter = new FileWriter(FUTURE_PROJECT_PATH, false);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(jsonAsString);
			bufferedWriter.write(LINE_ENDING);
			bufferedWriter.close();
		} catch (IOException e) {e.printStackTrace();
		} catch (JSONException e) {e.printStackTrace();}
	}
	
	
	/**
	 * Actions performed by buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(submitButton)){
			collectStartValuesFromGUI();
		}
		else if (e.getSource().equals(clearButton)){
			clearGUI();
		}
	}	
}
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

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
	// Contstants and indentifiers
	private static final String NO_INPUT = "0";
	private static final int ROWS = 5;
	private static final int COLUMNS = 4;
	private static final String IDENTIFIER = "#";
	private static final String LINE_ENDING = "\r\n";
	private static final String FUTURE_PROJECT_PATH = "files/futureproject.json";
	// Constants for the GUI
	private static final String WINDOW_TITLE = "Effort Estimation Calculator";
	private static final String RESULT_BUTTON_LABEL = "Result in [pm]";
	private static final String CLEAR_BUTTON_LABEL = "Clear";
	private static final String SUBMIT_BUTTON_LABEL = "Submit";
	private static final String ERR_MSG_INTERVAL = "Allowed: 1-6";
	private static final String ERR_MSG_FORMAT = "Enter number!";

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


	/*********************************************
	 * CONSTRUCTOR
	 *********************************************/

	/**
	 * Creates the GUI with default input values 
	 * and inits {@link EffortEstimation} with the database.
	 */
	public GUI(JSONObject database) {
		effortEstimation = new EffortEstimation(database);
		initGUI();
	}
	/** 
	 * Inits the GUI. 
	 * Creates the entire view. 
	 */
	private void initGUI() {
		
		JPanel mainPanel2  = new JPanel();
		// Label for the windows
		JFrame frame = new JFrame(WINDOW_TITLE);
		mainPanel = new JPanel(new GridLayout(ROWS, COLUMNS));
		
		
		// Program will exit when pressing the "x" in the top right corner
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Default size of the window
		frame.setSize(850, 600);
		
		// Inits buttons and result field and adds them to the layout
		submitButton = new JButton(SUBMIT_BUTTON_LABEL);
		clearButton = new JButton(CLEAR_BUTTON_LABEL);
		// Adds actionsListeners for both submit- and clear buttons
		submitButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		resultField = new JTextField();
		resultField.setText(RESULT_BUTTON_LABEL);
		mainPanel2.add(resultField, BorderLayout.SOUTH);
		mainPanel2.add(submitButton, BorderLayout.SOUTH);
		mainPanel2.add(clearButton, BorderLayout.SOUTH);

		matrixPanel = new JTextField[ROWS][COLUMNS];
		mainPanel2.add(submitButton);
		mainPanel2.add(clearButton);
		frame.add(mainPanel, BorderLayout.CENTER);
		frame.add(mainPanel2, BorderLayout.SOUTH);
		submitButton.addActionListener(this);

		// Creates the matrix view for the GUI 
		int index = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				JLabel typeLabel = new JLabel(EffortEstimation.TYPES[index++] + ": ", SwingConstants.RIGHT);
//				int textFieldSize = Math.max(ERR_MSG_INTERVAL.length(), ERR_MSG_FORMAT.length());
				int textFieldSize = 8;
				matrixBoxValue = new JTextField(textFieldSize);
				matrixBoxValue.setBackground(Color.LIGHT_GRAY);
				matrixBoxValue.setFont(new Font("Verdana", Font.BOLD, 10));
				matrixPanel[r][c] = matrixBoxValue;
				mainPanel.add(typeLabel);
				mainPanel.add(matrixBoxValue);
			}
		}
		frame.pack();
		frame.setVisible(true);
	}
	
	/*********************************************
	 * GUI METHODS
	 *********************************************/
	/**
	 * Collects the values that are inputed in GUI.
	 * If a field still has its default value, NO_INPUT is added to the the field.
	 * Calculates and returns the value of the estimation to the GUI
	 * and writes the entire input and effort estimation to files/futureproject.json
	 */
	public void collectStartValuesAndCalculateEffort()  {
		HashMap<String, String> project = new HashMap<String, String>();
		int index = 0;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if(matrixPanel[row][col].getText().equals("")){
					project.put(EffortEstimation.TYPES[index++].toLowerCase(), NO_INPUT);
				}else {
					project.put(EffortEstimation.TYPES[index++].toLowerCase(), matrixPanel[row][col].getText());
				}
			}
		}
//		Set<String> keys = project.keySet();
//		for (String s : keys) {
//			System.out.println("Key = " + s + " Value = " + project.get(s));
//		}
		String result = String.valueOf(effortEstimation.calculateEffortForProject(project));
		System.out.println(result);
		resultField.setText(result);
		project.put("effort[pm]", result);
		writeToFile(project);
	}
	
	private boolean validateStartValuesFromGUI(){
		boolean allFieldsValid = true;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				if(matrixPanel[row][col].getText().equals("")){
					matrixPanel[row][col].setBackground(Color.DARK_GRAY);
				} else if (row == 3 && col == 3) {
					// Size[kloc]
				}else{
					try{
						int value = Integer.parseInt((matrixPanel[row][col].getText()));
						if (value < 0 || value > 6){
							matrixPanel[row][col].setBackground(Color.RED);
							matrixPanel[row][col].setText(ERR_MSG_INTERVAL);
							allFieldsValid = false;
						}else{
							matrixPanel[row][col].setBackground(Color.LIGHT_GRAY);
						}
					}catch(NumberFormatException e){ 
						matrixPanel[row][col].setBackground(Color.RED);
						matrixPanel[row][col].setText(ERR_MSG_FORMAT);
						allFieldsValid = false;
					}
				}
			}
		}
		return allFieldsValid;
	}
	
	

	/**
	 * Clears the GUI from all inputs, to default values
	 */
	private void clearGUI() {
		// TODO: actionPerformed doesn't recognize that the user has pressed the Clear button. 
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				matrixPanel[row][col].setText("");
				matrixPanel[row][col].setBackground(Color.LIGHT_GRAY);
			}
		}
	}
	
	
	/**
	 * Actions performed by buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(submitButton)){
			if (validateStartValuesFromGUI()){
				collectStartValuesAndCalculateEffort();
			}
		}		
		else
			clearGUI();
	}	
	
	
	/*********************************************
	 * PRIVATE HELPER METHODS
	 *********************************************/
	
	
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
	
	
	
}
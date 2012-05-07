package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

import model.JSONDatabase;

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
	private static final String NO_INPUT = "-1";
	private static final int ROWS = 5;
	private static final int COLUMNS = 4;
	private static final String LINE_ENDING = "\r\n";
	private static final String FUTURE_PROJECT_PATH = "files/futureproject.json";
	// Constants for the GUI
	private static final String WINDOW_TITLE = "Effort Estimation Calculator";
	private static final String RESULT_BUTTON_LABEL = "Result in [pm]";
	private static final String CLEAR_BUTTON_LABEL = "Clear";
	private static final String SUBMIT_BUTTON_LABEL = "Submit";
	private static final String READ_DEFAULT_DATABASE_BUTTON_LABEL = "Read default databases";
	private static final String ERR_MSG_INTERVAL = "Allowed: 1-6";
	private static final String ERR_MSG_FORMAT = "Enter number!";

	/*********************************************
	 * CLASS OBJECTS
	 *********************************************/
	private JPanel mainPanelGrid;
	private JTextField matrixBoxValue;
	private JTextField[][] matrixPanel;
	private JLabel[] fieldLabels;
	private JButton submitButton;
	private JButton clearButton;
	private JButton readDefaultDatabaseButton;
	private JTextField databasePathField;
	private JButton addCustomDatabaseButton;
	private JTextArea resultArea;
	private JTextField thresholdField;
	private EffortEstimation effortEstimation;
	private JSONDatabase database;


	/*********************************************
	 * CONSTRUCTOR
	 *********************************************/
	

	/**
	 * Creates the GUI with default input values 
	 * and inits {@link EffortEstimation} with the database.
	 */
	public GUI() {
		this.database = JSONDatabase.getInstance();
		initGUI(database.getDefaultLabels());
	}
		
	/** 
	 * Inits the GUI. 
	 * Creates the entire view. 
	 */
	private void initGUI(String[] defaultLabels) {
		
		JPanel mainPanelSouthNorth  = new JPanel();
		JPanel mainPanelSouth = new JPanel();
		JPanel mainPanelNorth = new JPanel();
		// Label for the windows
		JFrame frame = new JFrame(WINDOW_TITLE);
		mainPanelGrid = new JPanel(new GridLayout(ROWS, COLUMNS));
		
		
		// Program will exit when pressing the "x" in the top right corner
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Default size of the window
		frame.setSize(850, 600);
		
		readDefaultDatabaseButton = new JButton(READ_DEFAULT_DATABASE_BUTTON_LABEL);
		mainPanelNorth.add(readDefaultDatabaseButton, BorderLayout.NORTH);
		mainPanelNorth.add(readDefaultDatabaseButton);
		readDefaultDatabaseButton.addActionListener(this);
		databasePathField = new JTextField("Enter path to database file");
		mainPanelNorth.add(databasePathField);
		addCustomDatabaseButton = new JButton("Use external database");
		mainPanelNorth.add(addCustomDatabaseButton);
		addCustomDatabaseButton.addActionListener(this);
		
		// Inits buttons and result field and adds them to the layout
		submitButton = new JButton(SUBMIT_BUTTON_LABEL);
		clearButton = new JButton(CLEAR_BUTTON_LABEL);
		// Adds actionsListeners for both submit- and clear buttons
		submitButton.addActionListener(this);
		clearButton.addActionListener(this);
		
		resultArea = new JTextArea(5,30);
		resultArea.setEditable(false);
		resultArea.setText("Result:");
		
		JLabel threshLbl = new JLabel("Threshold for similarity function (default = 80%):");
		thresholdField = new JTextField(5);
		
		mainPanelSouthNorth.add(resultArea, BorderLayout.NORTH);
		mainPanelSouthNorth.add(threshLbl, BorderLayout.SOUTH);
		mainPanelSouthNorth.add(thresholdField, BorderLayout.SOUTH);
		
		mainPanelSouth.add(mainPanelSouthNorth, BorderLayout.NORTH);
		
		mainPanelSouth.add(submitButton, BorderLayout.SOUTH);
		mainPanelSouth.add(clearButton, BorderLayout.SOUTH);

		matrixPanel = new JTextField[ROWS][COLUMNS];
		frame.add(mainPanelNorth, BorderLayout.NORTH);
		frame.add(mainPanelGrid, BorderLayout.CENTER);
		frame.add(mainPanelSouth, BorderLayout.SOUTH);

		// Creates the matrix view for the GUI 
		int index = 0;
		fieldLabels = new JLabel[ROWS * COLUMNS];
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				if(defaultLabels.length <= index || defaultLabels == null){
					fieldLabels[index] = new JLabel("Label " + index++ + ": ", SwingConstants.RIGHT);
				}else{
					fieldLabels[index] = new JLabel(defaultLabels[index++] + ": ", SwingConstants.RIGHT);
				}
				int textFieldSize = 8;
				matrixBoxValue = new JTextField(textFieldSize);
				matrixBoxValue.setBackground(Color.LIGHT_GRAY);
				matrixBoxValue.setFont(new Font("Verdana", Font.BOLD, 10));
				matrixPanel[r][c] = matrixBoxValue;
				mainPanelGrid.add(fieldLabels[index - 1]);
				mainPanelGrid.add(matrixBoxValue);
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
				String[] attributes = database.getDefaultLabels();
				if (attributes[index] != null) {
					if (matrixPanel[row][col].getText().equals("")){
						project.put(attributes[index++].toLowerCase(), NO_INPUT);
					}else {
						project.put(attributes[index++].toLowerCase(), matrixPanel[row][col].getText());
					}
				}
			}
		}
//		Set<String> keys = project.keySet();
//		for (String s : keys) {
//			System.out.println("Key = " + s + " Value = " + project.get(s));
//		}
		String result = String.valueOf(effortEstimation.calculateEffortForProject(project));
		int nbrProjects = effortEstimation.nbrOfProjectsInLastEstimation();
		resultArea.setText("Result:\n\n");
		resultArea.append("Estimated effort: " + result + " person months\n");
		resultArea.append("Based on " + nbrProjects + " projects");
		project.put("effort", result);
		writeToFile(project);
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
		else if (e.getSource().equals(clearButton)){
			clearGUI();
		}else if (e.getSource().equals(readDefaultDatabaseButton)){
			readDefaultDatabase();
			updateLabels(database.getDefaultLabels());
		} else if (e.getSource().equals(addCustomDatabaseButton)) {
			boolean madeIt = database.addDatabase(databasePathField.getText());
			if (!madeIt) {
				JOptionPane.showMessageDialog(null, "Error opening file or file is not a database file", "Error", JOptionPane.ERROR_MESSAGE);
			}
			effortEstimation = new EffortEstimation(database.getDatabaseAsJSONObject());
			updateLabels(database.getDefaultLabels());
		}
			
	}	
	
	
	private void updateLabels(String[] labels) {
		int index = 0;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				if(labels.length <= index || labels == null){
					fieldLabels[index].setText("Label " + index++ + ": ");
				}else{
					fieldLabels[index].setText(labels[index++] + ": ");
				}
			}
		}
	}

	/*********************************************
	 * PRIVATE HELPER METHODS
	 *********************************************/
	
	
	private void readDefaultDatabase() {
		database.readLocalDatabase();
		effortEstimation = new EffortEstimation(database.getDatabaseAsJSONObject());
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
		if (thresholdField.getText().isEmpty() || thresholdField.getText() == null) {
			effortEstimation.setThreshold(effortEstimation.SIMILARITY_THRESHOLD);
		} else {
			try {
				double threshold = Double.parseDouble(thresholdField.getText());
				effortEstimation.setThreshold(threshold / 100);
			} catch (NumberFormatException e) {
				thresholdField.setText(""+effortEstimation.SIMILARITY_THRESHOLD*100);
				effortEstimation.setThreshold(effortEstimation.SIMILARITY_THRESHOLD);
			}
		}
		return allFieldsValid;
	}
	
	

	/**
	 * Clears the GUI from all inputs, to default values
	 */
	private void clearGUI() { 
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				matrixPanel[row][col].setText("");
				matrixPanel[row][col].setBackground(Color.LIGHT_GRAY);
			}
		}
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
	
	
	
}
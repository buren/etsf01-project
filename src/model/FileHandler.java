package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import conversion.Converter;

public class FileHandler {

	/*********************************************
	 * class objects
	 *********************************************/
	private BufferedReader reader;
	private ArrayList<Integer> includedColumns;
	private String[] colNames;

	/*********************************************
	 * constructor
	 *********************************************/
	public FileHandler() {
		includedColumns = new ArrayList<Integer>();
		colNames = new String[20];
	}

	/*********************************************
	 * public methods
	 *********************************************/

	/**
	 * Reads the format specifications of a database file (lines beginning with
	 * #option.
	 * I'm thinking the format could be something like:
	 * #OPTION PATH <path>
	 * #OPTION DELIM <delimiters>
	 * #OPTION IGNORE <ignore pattern>
	 * #OPTION COLS <included columns>
	 * #OPTION VALUES <valuename1,valuename2,...,valuename6>
	 * #OPTION TIME <time unit>
	 */
	public JSONObject readOptions(String inputPath) {
		JSONObject opts = new JSONObject();
		try {
			reader = new BufferedReader(new FileReader(inputPath));
			String line = sanitize(reader.readLine());
			while (line != null && !line.startsWith("#option")) {
				line = sanitize(reader.readLine());
			}
			int nbrOfOptsFound = 0;
			try {
				while (nbrOfOptsFound < 5) {
					if (line == null || !line.startsWith("#option")) {
						//File does not follow filespec
						return null;
					}
					line = line.substring(7);
					if (line.startsWith("delim") && !opts.has("delim")) {
						opts.put("delim", line.substring(5));
						nbrOfOptsFound++;
					} else if (line.startsWith("ignore") && !opts.has("ignore")) {
						opts.put("ignore", line.substring(6));
						nbrOfOptsFound++;
					} else if (line.startsWith("cols") && !opts.has("cols")) {
						opts.put("cols", line.substring(4));
						nbrOfOptsFound++;
					} else if (line.startsWith("values") && !opts.has("labels")) {
						opts.put("values", line.substring(6).split(","));
						nbrOfOptsFound++;
					} else if (line.startsWith("time") && !opts.has("time")) {
						opts.put("time", Integer.parseInt(line.substring(4)));
						nbrOfOptsFound++;
					}
					line = sanitize(reader.readLine());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return opts;
	}

	/**
	 * Reads the database file and returns it as a {@link JSONObject}.
	 * 
	 * @param inputPath
	 *            - full path for the database file
	 * @param delimiter
	 *            - delimiter between the columns
	 * @param ignorePattern
	 *            - lines beginning with each character in this string will be
	 *            ignored
	 * @param includedColumns
	 *            - column indices to include (e.i {0,2,5..10}
	 * @param valueNames
	 *            - if the values in the columns aren't numeric input the
	 *            corresponding string i.e valueNames[0] = "very_low" will give
	 *            every column with the value very_low the numeric value of 2
	 * @param timeUnit
	 *            - time unit (person-, years, months, days, hours) for
	 *            effort[pX] column
	 * @return all retrieved projects in a JSONObject
	 */
	public JSONObject readDatabase(String inputPath, String delimiter,
			String ignorePattern, String includedColumns, String[] valueNames,
			int timeUnit) {
		this.includedColumns.clear();
		JSONObject responsJSON = new JSONObject();

		// Constructs the array with all every column index that the user wants
		// to include.
		constructIncludedColumns(sanitize(includedColumns));
		ArrayList<HashMap<String, String>> projectList = new ArrayList<HashMap<String, String>>();

		// Tries to read the defined file.
		try {
			reader = new BufferedReader(new FileReader(inputPath));
			String line = sanitize(reader.readLine());

			// Identifiers for each project
			while (ignoreLine(line,
					constructIgnorePattern(sanitize(ignorePattern))))
				line = sanitize(reader.readLine());
			// Gets the first readable line that contains all the column names
			String[] columnNames;
			// If statement to check whether the are any
			// accepted lines in the file after the ignored ones.
			if (line != null) {
				columnNames = line.split(",");
				line = sanitize(reader.readLine());
			} else {
				return null;
			}

			while (line != null) {
				// Adds each project (one line) to projectList
				projectList.add(addAttributesFromLine(line, delimiter,
						valueNames, columnNames, timeUnit));
				line = sanitize(reader.readLine());
			}
			// Adds all the projects to responsJSON with associated UUID
			for (HashMap<String, String> map : projectList)
				responsJSON.put(String.valueOf(UUID.randomUUID()), map);

		} catch (FileNotFoundException e) {
			System.err.println("FileHandler: File not found exception!");
		} catch (IOException e) {
			System.err.println("FileHandler I/O exception!");
		} catch (JSONException e) {
			System.err.println("FileHandler JSONException!");
		}
		return responsJSON;
	}

	public String[] getCurrentLabels() {
		return colNames;
	}

	/**
	 * Writes jsonObject to file <i>{@value DATABASE_OUTPUT_PATH}</i>.
	 * 
	 * @param path
	 */
	public void writeJSONtoFile(String path, JSONObject jsonObject) {
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(path);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(jsonObject.toString(2));
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/*********************************************
	 * private helper methods
	 *********************************************/
	
	// TODO Update the following JavaDoc please!
	/**
	 * Sets the correct values for each column depending on which type of file
	 * that is read
	 * 
	 * @param types
	 *            - list of the types that is used
	 * @param lineAttributes
	 *            - string array with all the values for each column
	 * @param fileType
	 *            - which type of input file is it?
	 * @return a HashMap containing all the data.
	 */
	private HashMap<String, String> addAttributesFromLine(String line,
			String delimiter, String[] valueNames, String[] columnNames,
			int timeUnit) {
		String[] attributes = sanitize(line).split(delimiter);
		HashMap<String, String> project = new HashMap<String, String>();
		int columnNamesIndex = 0;
		colNames = new String[20];
		for (Integer index : includedColumns) {
			if (columnNames[index].contains("effort")) {
				project.put(columnNames[index], String.valueOf(Converter
						.convertToHours(timeUnit,
								Double.parseDouble(attributes[index]))));
				colNames[columnNamesIndex++] = String
						.valueOf(columnNames[index]);
			} else {
				project.put(columnNames[index],
						convertToDigits(attributes[index], valueNames));
				colNames[columnNamesIndex++] = String
						.valueOf(columnNames[index]);
			}
		}
		return project;
	}

	/**
	 * Converts the "natural" language attributes to values according to user
	 * defined list. If its already a number the argument is returned.
	 * 
	 * @param attr
	 *            the attribute
	 * @return the values containing only digits.
	 */
	private String convertToDigits(String attr, String[] valueNames) {
		if (!Character.isDigit((attr.charAt(0)))) {
			for (int index = 0; index < valueNames.length; index++) {
				if (valueNames[index].equals(attr))
					return String.valueOf(index);
			}
		}
		return attr;
	}

	/**
	 * Simple helper method which simply just returns the argument trimmed and
	 * as a lower case string.
	 * 
	 * @param arg
	 *            the argument to be sanitized.
	 * @return a string which is trimmed and converted to lowercase.
	 */
	private String sanitize(String arg) {
		String result = arg;
		if (result != null) {
			result = result.replace(" ", "");
			result = result.toLowerCase();
			result = result.trim();
			return result;
		}
		return null;
	}

	/**
	 * Checks whether the lined should be ignored.
	 * 
	 * @param delimiter
	 * @param ignorePattern
	 * @return true if the line shall be ignored false otherwise.
	 */
	private boolean ignoreLine(String line, char[] ignorePattern) {
		if (line.equals("") || line.equals("\n"))
			return true;
		for (char ch : ignorePattern)
			if (line.charAt(0) == ch)
				return true;
		return false;
	}

	/**
	 * Receives a string of characters. Each character in the string will
	 * represent a character in the ignorePattern char[].
	 * 
	 * @param ignorePattern
	 *            each character what will be ignored, not including whitespace
	 * @return the ingoredPattern as char[]
	 */
	private char[] constructIgnorePattern(String ignorePattern) {
		char[] ignoredChars = new char[ignorePattern.length()];
		for (int i = 0; i < ignorePattern.length(); i++)
			ignoredChars[i] = ignorePattern.charAt(i);
		return ignoredChars;
	}

	/**
	 * Adds all accepted columns to an array.
	 * 
	 * @param includedColumns
	 *            the columns to be included "1-20" argument will result in the
	 *            included columns {1, 2, 3, .. , 19, 20) "1,2,6" argument will
	 *            reuslt in {1,2,6} "1,5,8-12" argument will result in
	 *            {1,5,8,9,10,11,12}
	 */
	private ArrayList<Integer> constructIncludedColumns(String includedCols) {
		String[] input = includedCols.split(",");
		for (String value : input) {
			String[] attr = value.split("-");
			if (attr.length > 1)
				addRangeToArray(attr[0], attr[1]);
			else
				addRangeToArray(attr[0], attr[0]);
		}
		return includedColumns;
	}

	/**
	 * Adds the specified range firstNumber-lastNumber to the array of accepted
	 * columns.
	 * 
	 * @param firstNumber
	 * @param lastNumber
	 */
	private void addRangeToArray(String firstNumber, String lastNumber) {
		int first = Integer.parseInt(firstNumber);
		int last = Integer.parseInt(lastNumber);
		if (first == last && !includedColumns.contains(first))
			includedColumns.add(first);
		else {
			for (int i = first; i <= last; i++)
				if (!includedColumns.contains(i))
					includedColumns.add(i);
		}
		Collections.sort(includedColumns);
	}

}

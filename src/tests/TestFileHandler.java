package tests;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import model.FileHandler;
import model.JSONDatabase;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;

import conversion.Converter;

public class TestFileHandler extends TestCase {
	

	/********************************************
	 * private constants
	 ********************************************/
	private static final String EFFORTPM_COLUMN = "effort[pm]";
	private static final String CPLX_COLUMN = "cplx";
	private static final String RELY_COLUMN = "rely";
	// First line values
	private static final String FIRST_LINE_RELY_VALUE = "2";
	private static final String FIRST_LINE_CPLX_VALUE = "4";
	private static final String FIRST_LINE_EFFORT_VALUE = "42256.0";

	// Last line values
	private static final String LAST_LINE_RELY_VALUE = "3";
	private static final String LAST_LINE_CPLX_VALUE = "4";
	private static final String LAST_LINE_EFFORT_VALUE = "5776.0";

	// Index for first and last lines
	private static final String FIRST_LINE_INDEX = "0";
	private static final String LAST_LINE_INDEX = "152";
	
	private static final String[] VALUE_NAMES_FIRST = {"very_low", "low", "nominal",  "high", "very_high", "extra_high"};
	private static final String[] VALUE_NAMES_SECOND = {"vl", "l", "n", "h", "vh", "xh"};
	
	
	

	/********************************************
	 * test class objects
	 ********************************************/
	private FileHandler fileHandler;
	private JSONDatabase database;
	private JSONObject jsonObject;
	
	
	@Before
	public void setUp(){
		database = JSONDatabase.getInstance();	
		fileHandler = new FileHandler();
	}
	
	
	@Test
	public void testReadFirstDatabase() throws JSONException{
		JSONTokener tokener = new JSONTokener(database.getDatabaseAsJSONObject().toString(2));
		jsonObject = new JSONObject(tokener);
	}
	
	
	
	/********************************************
	 * test for private methods in FileHandler
	 ********************************************/
	
	@Test
	public void testConvertToDigits() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method method = FileHandler.class.getDeclaredMethod("convertToDigits", String.class, String[].class);
		method.setAccessible(true);
		String[] vec = {"hallo", "eller"};
		String output = (String) method.invoke(fileHandler, "10", vec);
		assertEquals("Failed to convert a number from a number!", "10", output);
		output = (String) method.invoke(fileHandler, "hallo", vec);
		assertEquals("Failed to convert a string to a number for userdefined value", "0", output);
	}
	
	public void testSanitize() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method method = FileHandler.class.getDeclaredMethod("sanitize", String.class);
		method.setAccessible(true);
		String output = (String) method.invoke(fileHandler, " HEjsan  ! ");
		assertEquals("Failed to sanitize string! ", "hejsan!", output);
		output = (String) method.invoke(fileHandler, (Object) null);
		assertNull("Failed to return null when using null as an argument",output);
	}
	
	public void testIgnoreLine() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method method = FileHandler.class.getDeclaredMethod("ignoreLine", String.class, char[].class);
		method.setAccessible(true);
		char[] vec = {'@', '%'};
		assertTrue("Failed to ignore line", (Boolean) method.invoke(fileHandler, "@hejsanhejsan", vec));
		assertFalse("Ignored line that should have been read", (Boolean) method.invoke(fileHandler, "hejsanhejsan", vec));
	}
	
	public void testAddRangeToarray() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
			Method method = FileHandler.class.getDeclaredMethod("constructIncludedColumns", String.class);
			method.setAccessible(true);
			ArrayList<Integer> list = (ArrayList<Integer>) method.invoke(fileHandler, "1-4");
			ArrayList<Integer> list1 = (ArrayList<Integer>) method.invoke(fileHandler, "1");
			int result = list.get(0);
			assertEquals("Failed to create range 1-4", 1, result);
			result = list.get(1);
			assertEquals("Failed to create range 1-4", 2, result);
			result = list.get(2);
			assertEquals("Failed to create range 1-4", 3, result);
			result = list.get(3);
			assertEquals("Failed to create range 1-4", 4, result);
			result = list1.get(0);
			assertEquals("Failed to create range 1-1", 1, result);
	}
	
	public void testCounstructIncludedColumns() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method method = FileHandler.class.getDeclaredMethod("constructIncludedColumns", String.class);
		method.setAccessible(true);
		ArrayList<Integer> list = (ArrayList<Integer>) method.invoke(fileHandler, "1,2,4-5,7");
		int result = list.get(0);
		assertEquals("Failed to create range 1,2,4-5,7", 1, result);
		result = list.get(1);
		assertEquals("Failed to create range 1,2,4-5,7", 2, result);
		result = list.get(2);
		assertEquals("Failed to create range 1,2,4-5,7", 4, result);
		result = list.get(3);
		assertEquals("Failed to create range 1,2,4-5,7", 5, result);
		result = list.get(4);
		assertEquals("Failed to create range 1,2,4-5,7", 7, result);

	}
	
	public void testConstructIgnorePattern() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Method method = FileHandler.class.getDeclaredMethod("constructIgnorePattern", String.class);
		method.setAccessible(true);
		char[] result = (char[]) method.invoke(fileHandler, "%@#");
		assertEquals("Failed to construct ignore pattern", result[0], '%');
		assertEquals("Failed to construct ignore pattern", result[1], '@');
		assertEquals("Failed to construct ignore pattern", result[2], '#');
	}
	
	
}

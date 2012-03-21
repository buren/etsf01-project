package tests;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import model.JSONDatabase;

public class TestJSONDatabase extends TestCase{
	private JSONDatabase json;
	private static final String TEST_STRING_RELY_FOR_FIRST_LINE = "Nominal";
	private static final String TEST_STRING_RELT_FOR_LAST_LINE = "Nominal";
	private static final String TEST_STRING_EFFORT_FOR_FIRST_LINE = "278";
	private static final String TEST_STRING_EFFORT_FOR_LAST_LINE = "155";
	private static final String FIRST_LINE_INDEX = "1";
	private static final String LAST_LINE_INDEX = "59";
	
	@Before
	public void setUp(){
		json = JSONDatabase.getInstance();
	}
	
	@Test
	public void testReadFromDatabaseINFile(){
	}
	
}

package tests;

import org.junit.Before;
import org.junit.Test;

import conversion.Converter;

import junit.framework.TestCase;

public class TestConverter extends TestCase{
	
	private static final double ERROR_CORRECTION_TWO = 0.00000000000002;
	private static final double ERROR_CORRECTION_ONE = 0.000000000000001;
	/*********************************************
	 * private constants
	 *********************************************/
	private static final int HOURS_IN_YEAR = 1824;
	private static final int HOURS_IN_MONTH = 152;
	private static final int HOURS_IN_DAY = 8;
	private static final int DAYS_IN_MONTH = 19;
	private static final int DAYS_IN_YEAR = 228;
	private static final int MONTHS_IN_YEAR = 12;
	
	/*********************************************
	 * class objects
	 *********************************************/
	private double value;
	
	@Before
	public void setUp(){
		value = 14.2;
	}
	
	@Test
	public void testConvertToHours(){
		assertEquals(value, Converter.convertToHours(Converter.HOURS, value));
		assertEquals(value*HOURS_IN_DAY, Converter.convertToHours(Converter.DAYS, value));
		assertEquals(value*HOURS_IN_MONTH, Converter.convertToHours(Converter.MONTHS, value));
		assertEquals(value*HOURS_IN_YEAR, Converter.convertToHours(Converter.YEARS, value));
	}
	
	@Test
	public void testConvertToDays(){
		assertEquals(value/HOURS_IN_DAY, Converter.convertToDays(Converter.HOURS, value));
		assertEquals(value, Converter.convertToDays(Converter.DAYS, value));
		assertEquals(value*DAYS_IN_MONTH, Converter.convertToDays(Converter.MONTHS, value));
		assertEquals(value*DAYS_IN_YEAR, Converter.convertToDays(Converter.YEARS, value));
	}
	
	@Test
	public void testConvertToMonths(){
		assertEquals(value/HOURS_IN_MONTH, Converter.convertToMonths(Converter.HOURS, value));
		assertEquals(value/DAYS_IN_MONTH, Converter.convertToMonths(Converter.DAYS, value));
		// ERROR_CORRECTION_* is for correction java Math.round error
		assertEquals(value+ERROR_CORRECTION_ONE, Converter.convertToMonths(Converter.MONTHS, value));
		assertEquals(value*MONTHS_IN_YEAR + ERROR_CORRECTION_TWO, Converter.convertToMonths(Converter.YEARS, value));
	}
	
	@Test
	public void testConvertToYears(){
		assertEquals(value/HOURS_IN_YEAR, Converter.convertToYears(Converter.HOURS, value));
		assertEquals(value/DAYS_IN_YEAR, Converter.convertToYears(Converter.DAYS, value));
		assertEquals(value/MONTHS_IN_YEAR, Converter.convertToYears(Converter.MONTHS, value));
		assertEquals(value, Converter.convertToYears(Converter.YEARS, value));
	}
}

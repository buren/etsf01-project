package tests;

import org.junit.Before;
import org.junit.Test;

import conversion.Converter;

import junit.framework.TestCase;

public class TestConverter extends TestCase{
	private double value1;
	private double value2;
	private double value3;
	
	@Before
	public void setUp(){
		value1 = 110;
		value2 = 14.2;
		value3 = 32.1;
	}
	
	@Test
	public void testConvertToHours(){
		assertEquals(value1, Converter.convertToHours(Converter.HOURS, value1));
		assertEquals(value2, Converter.convertToHours(Converter.HOURS, value2));
		assertEquals(value3, Converter.convertToHours(Converter.HOURS, value3));
	}
	
	@Test
	public void testConvertToDays(){
		assertEquals(value1*24, Converter.convertToDays(Converter.DAYS, value1));
		assertEquals(value2*24, Converter.convertToDays(Converter.DAYS, value2));
		assertEquals(value3*24, Converter.convertToDays(Converter.DAYS, value3));
	}
	
	@Test
	public void testConvertToMonths(){
		assertEquals(value1*720, Converter.convertToMonths(Converter.MONTHS, value1));
		assertEquals(value2*720, Converter.convertToMonths(Converter.MONTHS, value2));
		assertEquals(value3*720, Converter.convertToMonths(Converter.MONTHS, value3));
	}
	
	@Test
	public void testConvertToYears(){
		assertEquals(value1*262800, Converter.convertToYears(Converter.YEARS, value1));
		assertEquals(value2*262800, Converter.convertToYears(Converter.YEARS, value2));
		assertEquals(value3*262800, Converter.convertToYears(Converter.YEARS, value3));
	}
}

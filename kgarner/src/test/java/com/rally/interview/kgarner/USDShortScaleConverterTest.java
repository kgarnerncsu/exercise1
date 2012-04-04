package com.rally.interview.kgarner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link USDShortScaleConverter} {@link AmountToTextConverter}.
 * 
 * @author kgarner
 */
public class USDShortScaleConverterTest {
	private AmountToTextConverter converter;
	
	private BigDecimal d(String val) {
		return new BigDecimal(val);
	}

	@Before
	public void setUp() {
		converter = new USDShortScaleConverter();
	}

	@Test
	public void convert0Cents() {
		String text = converter.toString(d("0"));
		assertThat(text, is("Zero and 00/100 dollars"));
	}

	@Test
	public void convert1Cent() {
		String text = converter.toString(d("0.01"));
		assertThat(text, is("Zero and 01/100 dollars"));
	}

	@Test
	public void convert99Cents() throws Exception {
		String text = converter.toString(d("0.99"));
		assertThat(text, is("Zero and 99/100 dollars"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void convertNegativeDecimalError() throws Exception {
		converter.toString(d("-1"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertNegativeDollarsError() throws Exception {
		converter.toString(-1, 0);
	}
	
	@Test
	public void convert12_999cents() throws Exception {
		// Should ignore partial cents
		String text = converter.toString(d("12.999"));
		assertThat(text, is("Thirteen and 00/100 dollars"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertNegativeCentsError() throws Exception {
		converter.toString(0, -1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertTooManyCentsError() throws Exception {
		converter.toString(0, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void convertBigMoneyError() throws Exception {
		converter.toString(d("1e15"));
	}

	@Test
	public void convertDollarsNoCents() throws Exception {
		String text = converter.toString(d("1"));
		assertThat(text, is("One and 00/100 dollars"));
	}

	@Test
	public void convert10Dollars() throws Exception {
		String text = converter.toString(d("10"));
		assertThat(text, is("Ten and 00/100 dollars"));
	}

	@Test
	public void convert11Dollars() throws Exception {
		String text = converter.toString(d("11"));
		assertThat(text, is("Eleven and 00/100 dollars"));
	}

	@Test
	public void convert12Dollars() throws Exception {
		String text = converter.toString(d("12"));
		assertThat(text, is("Twelve and 00/100 dollars"));
	}

	@Test
	public void convert13Dollars() throws Exception {
		String text = converter.toString(d("13"));
		assertThat(text, is("Thirteen and 00/100 dollars"));
	}

	@Test
	public void convert20Dollars() throws Exception {
		String text = converter.toString(d("20"));
		assertThat(text, is("Twenty and 00/100 dollars"));
	}

	@Test
	public void convert23Dollars() throws Exception {
		String text = converter.toString(d("23"));
		assertThat(text, is("Twenty-three and 00/100 dollars"));
	}

	@Test
	public void convert100Dollars() throws Exception {
		String text = converter.toString(d("100"));
		assertThat(text, is("One hundred and 00/100 dollars"));
	}

	@Test
	public void convert105Dollars() throws Exception {
		String text = converter.toString(d("105"));
		assertThat(text, is("One hundred five and 00/100 dollars"));
	}

	@Test
	public void convert137Dollars() throws Exception {
		String text = converter.toString(d("137"));
		assertThat(text, is("One hundred thirty-seven and 00/100 dollars"));
	}

	@Test
	public void convertThousandDollars() throws Exception {
		String text = converter.toString(d("1000"));
		assertThat(text, is("One thousand and 00/100 dollars"));
	}

	@Test
	public void convertMillionDollars() throws Exception {
		String text = converter.toString(d("1e6"));
		assertThat(text, is("One million and 00/100 dollars"));
	}

	@Test
	public void convertBillionDollars() throws Exception {
		String text = converter.toString(d("1e9"));
		assertThat(text, is("One billion and 00/100 dollars"));
	}

	@Test
	public void convertTrillionDollars() throws Exception {
		String text = converter.toString(d("1e12"));
		assertThat(text, is("One trillion and 00/100 dollars"));
	}
	
	@Test
	public void convertAlmostBigMoney() throws Exception {
		String text = converter.toString(d("1e15").subtract(d("0.01")));
		assertThat(text, is("Nine hundred ninety-nine trillion nine hundred ninety-nine billion nine hundred ninety-nine million nine hundred ninety-nine thousand nine hundred ninety-nine and 99/100 dollars"));
	}

	@Test
	public void convert1381212_18DollarsUsingDouble() throws Exception {
		// Tests double precision issues (non-issue with bigdecimal)
		String text = converter.toString(d("1381212.18"));
		assertThat(
				text,
				is("One million three hundred eighty-one thousand two hundred twelve and 18/100 dollars"));
	}

	@Test
	public void convert1381212_18DollarsUsingLongs() throws Exception {
		String text = converter.toString(1381212, 18);
		assertThat(
				text,
				is("One million three hundred eighty-one thousand two hundred twelve and 18/100 dollars"));
	}

	@Test
	public void convert2523_04Dollars() throws Exception {
		String text = converter.toString(d("2523.04"));
		assertThat(text,
				is("Two thousand five hundred twenty-three and 04/100 dollars"));
	}

	@Test
	public void convertJavadocEx() throws Exception {
		String text = converter.toString(d("1000002523.04"));
		assertThat(
				text,
				is("One billion two thousand five hundred twenty-three and 04/100 dollars"));
	}
}

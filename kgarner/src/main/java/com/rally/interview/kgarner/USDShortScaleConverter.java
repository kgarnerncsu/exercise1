package com.rally.interview.kgarner;

import java.math.BigDecimal;
import java.util.LinkedList;

import com.google.common.base.Preconditions;

/**
 * This {@link AmountToTextConverter} transforms a number into a phrase that
 * would be used to represent an amount of US dollars, such as is used when
 * writing a check. Values over one million use the short scale convention. Max
 * value is one less than one quadrillion. For example:
 * 
 * <pre>
 * 1,000,002,523.04 => One billion two thousand five hundred twenty-three and 04/100 dollars
 * </pre>
 * 
 * @author kgarner
 */
public class USDShortScaleConverter implements AmountToTextConverter {
	private static final String[] ZERO = { "zero" };
	private static final String[] SCALE_POSITIONS = { null, "thousand",
			"million", "billion", "trillion" };
	private static final BigDecimal MAX_EXCLUSIVE = new BigDecimal("1e15");

	/**
	 * Converts decimal dollars to word representation. Rounds to nearest cent.
	 * See preconditions in other method.
	 * 
	 * @see #toString(long, long)
	 * @see com.rally.interview.kgarner.AmountToTextConverter#toString(double)
	 */
	public String toString(BigDecimal dollars) {
		Preconditions.checkNotNull(dollars, "Dollars must not be null");
		Preconditions.checkArgument(dollars.signum() != -1,
				"Dollars cannot be negative");
		Preconditions.checkArgument(dollars.compareTo(MAX_EXCLUSIVE) <= 0,
				"Dollars cannot be more than %s", MAX_EXCLUSIVE);
		// Convert to pennies, rounding using half up along the way
		long pennies = dollars.movePointRight(2)
				.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
		// retain remainder pennies
		long cents = pennies % 100;
		// extract dollars from pennies
		long wholeDollars = pennies / 100;
		return toString(wholeDollars, cents);
	}

	/**
	 * Convert a split dollar amount (integer and fractional parts) into a word
	 * representation.
	 * 
	 * @see com.rally.interview.kgarner.AmountToTextConverter#toString(long,
	 *      long)
	 * @throws IllegalArgumentException
	 *             if negative dollars or cents, >= 1e18 dollars, >= 100 cents
	 */
	public String toString(long dollars, long cents) {
		Preconditions.checkArgument(dollars >= 0, "Dollars cannot be negative");
		Preconditions.checkArgument(cents >= 0, "Cents cannot be negative");
		Preconditions.checkArgument(cents < 100, "Cents must be less than 100");
		Preconditions.checkArgument(dollars < MAX_EXCLUSIVE.longValueExact(),
				"Dollars cannot be more than %s", MAX_EXCLUSIVE);
		StringBuilder text = new StringBuilder();
		boolean first = true;
		for (String segment : formatWhole(dollars)) {
			if (first) {
				segment = Character.toUpperCase(segment.charAt(0))
						+ segment.substring(1);
				first = false;
			}
			text.append(segment).append(" ");
		}
		text.append("and ");
		text.append(formatCents(cents));
		text.append(" dollars");
		return text.toString();
	}

	/**
	 * Converts whole part of dollar value into string
	 * 
	 * @param dollars
	 *            Whole portion of dollar value
	 * @return String representation of whole dollars
	 */
	private String[] formatWhole(long dollars) {
		if (dollars == 0) {
			return ZERO;
		}
		LinkedList<String> number = new LinkedList<String>();
		for (int thousandsPower = 0; dollars > 0; thousandsPower++) {
			long max = (long) Math.pow(1000, thousandsPower + 1);
			long remainder = dollars % max;
			if (thousandsPower > 0 && remainder > 0) {
				number.push(SCALE_POSITIONS[thousandsPower]);
			}
			long shrunk = remainder / (long) Math.pow(1000, thousandsPower);
			long subhundred = shrunk % 100;
			if (subhundred > 0) {
				if (subhundred < 20) {
					number.push(formatSingle(subhundred));
				} else {
					String tens = formatTens(subhundred / 10);
					long singles = subhundred % 10;
					if (singles > 0) {
						number.push(tens + "-" + formatSingle(subhundred % 10));
					} else {
						number.push(tens);
					}
				}
			}
			if (shrunk >= 100) {
				number.push("hundred");
				number.push(formatSingle(shrunk / 100));
			}
			dollars = dollars - remainder;
		}
		return (String[]) number.toArray(new String[number.size()]);
	}

	/**
	 * For 2-9, format the number as the tens version word representation
	 * (twenty-ninety)
	 * 
	 * @param number
	 *            Tens digit
	 * @return Tens digit name
	 */
	private String formatTens(long number) {
		switch ((int) number) {
		case 2:
			return "twenty";
		case 3:
			return "thirty";
		case 4:
			return "forty";
		case 5:
			return "fifty";
		case 6:
			return "sixty";
		case 7:
			return "seventy";
		case 8:
			return "eighty";
		case 9:
			return "ninety";
		}
		throw new IllegalArgumentException("Unexpected integer value: "
				+ number);
	}

	/**
	 * For numbers less than 20, their word representation
	 * 
	 * @param number
	 *            Number less than 20
	 * @return Name of number
	 */
	private String formatSingle(long number) {
		switch ((int) number) {
		case 1:
			return "one";
		case 2:
			return "two";
		case 3:
			return "three";
		case 4:
			return "four";
		case 5:
			return "five";
		case 6:
			return "six";
		case 7:
			return "seven";
		case 8:
			return "eight";
		case 9:
			return "nine";
		case 10:
			return "ten";
		case 11:
			return "eleven";
		case 12:
			return "twelve";
		case 13:
			return "thirteen";
		case 14:
			return "fourteen";
		case 15:
			return "fifteen";
		case 16:
			return "sixteen";
		case 17:
			return "seventeen";
		case 18:
			return "eighteen";
		case 19:
			return "nineteen";
		}
		throw new IllegalArgumentException("Unexpected integer value: "
				+ number);
	}

	/**
	 * Format cents portion of amount as xx/100
	 * 
	 * @param cents
	 *            Cents to output
	 * @return formatted cents
	 */
	private String formatCents(long cents) {
		return String.format("%02d/100", cents);
	}

}

package com.rally.interview.kgarner;

import java.math.BigDecimal;

/**
 * Interface for converter that transforms an amount into a word representation.
 * 
 * @author kgarner
 */
public interface AmountToTextConverter {

	/**
	 * Convert an integer and fractional portion of a number to a word representation.
	 * @param integer Integer portion of number
	 * @param fraction Fractional portion of number
	 * @return Words representing number
	 */
	String toString(long integer, long fraction);

	/**
	 * Convert a decimal number to a word representation.
	 * @param number Decimal number
	 * @return Words representing number
	 */
	String toString(BigDecimal number);
}
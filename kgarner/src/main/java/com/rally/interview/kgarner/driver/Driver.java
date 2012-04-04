package com.rally.interview.kgarner.driver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import com.rally.interview.kgarner.USDShortScaleConverter;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		USDShortScaleConverter converter = new USDShortScaleConverter();
		BigDecimal amount = null;
		while (amount == null) {
			System.out.print("Please enter an amount to convert: ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				amount = new BigDecimal(reader.readLine());
			} catch (Exception e) {
				System.err
						.println("Unable to read value. Please ensure the amount was valid and try again.");
			}
			try {
				String text = converter.toString(amount);
				System.out.printf("Amount: %s", text);
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
}

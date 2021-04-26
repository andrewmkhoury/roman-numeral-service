/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.akhoury.romannumeral;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with a method for converting positive integers in the range 1-3999 to a Roman Numeral string.
 */
public class RomanNumeralConverter {
	
	private final static Logger log = LoggerFactory.getLogger(RomanNumeralConverter.class);
	
	/**
	 * All unique Roman Numeral values in descending order.
	 */
	enum RomanNumeral {
		M(1000), CM(900), D(500), CD(400), C(100), XC(90), L(50), XL(40), X(10), IX(9), V(5), IV(4), I(1);

		final int value;

		RomanNumeral(int value) {
			this.value = value;
		}
	};

	/**
	 * Converts a positive integer in the range 1-3999 to a Roman
	 * Numeral string representation of the same number.
	 * 
	 * @param positiveInteger positive integer value in the range 1-3999.
	 * @return converted number in roman numeral.
	 * @throws InputOutOfSupportedRangeException if the parameter is out of the range 1-3999.
	 */
	public static String convertToRomanNumeral(int positiveInteger) throws InputOutOfSupportedRangeException {
		if(positiveInteger < 1 || positiveInteger > 3999) {
			throw new InputOutOfSupportedRangeException(Integer.toString(positiveInteger));
		}

		StringBuffer romanNumeralStr = new StringBuffer();
		// setting variable to name remainder for easier reading of the algorithm
		int remainder = positiveInteger;
		RomanNumeral[] numeralsArr = RomanNumeral.values();
		
		// For the sake of maintainability and readability copy the unique Roman Numeral values from an enum to a LIFO queue.
		// Note that a two array implementation (int array and String array) would have slightly better performance,
		// however the code is more maintainable this way.
		// With the enum values in the queue, iterate, dequeuing from highest to lowest unique Roman Numeral and divide
		// finding the first value that yields a non-zero quotient.  Take the remainder at each pass and repeat until the queue is empty.
		Queue<RomanNumeral> numerals = new LinkedList<RomanNumeral>();
		Collections.addAll(numerals, numeralsArr); // add the numerals to a linked list so we can dequeue them as we divide
		while (numerals.size() > 0) {
			for (int i = 0; i < numerals.size(); i++) {
				RomanNumeral romanNumeral = numerals.remove();
				int quotient = remainder / romanNumeral.value; // Math.floor() not required because Java rounds down
				if(log.isTraceEnabled()) { // Added since the test mentioned to add logging
					log.trace("{}/{}={} will append {} {} time(s)", remainder, romanNumeral.value, quotient, romanNumeral, quotient);
				}
				if (quotient >= 1) {
					for (int q = quotient; q > 0; q--) {
						romanNumeralStr.append(romanNumeral);
					}
					// Get the remainder
					remainder = remainder % romanNumeral.value;
					if (remainder == 0)
						break; //break from the loop when the remainder is zero
				}
			}
		}

		return romanNumeralStr.toString();
	}
}

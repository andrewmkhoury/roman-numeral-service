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

/**
 * 
 */
public class RomanNumeralConverter {
	/**
	 * All unique roman numeral values in descending order.
	 */
	enum RomanNumeral {
		M(1000), CM(900), D(500), CD(400), C(100), XC(90), L(50), XL(40), X(10), IX(9), V(5), IV(4), I(1);

		final int value;

		RomanNumeral(int value) {
			this.value = value;
		}
	};

	/**
	 * Takes in a positive integer in the range 1-3999 and converts it to a roman
	 * numeral string representation of the same number.
	 * 
	 * @param positiveInteger positive integer value in the range 1-3999.
	 * @return converted number in roman numeral.
	 * @throws InputOutOfSupportedRangeException if the parameter is out of the range 1-3999.
	 */
	public static String convertToRomanNumeral(int positiveInteger) throws InputOutOfSupportedRangeException {
		if(positiveInteger < 1 || positiveInteger > 3999) {
			throw new InputOutOfSupportedRangeException(Integer.toString(positiveInteger));
		}
		// Roman numeral specification https://www.mathsisfun.com/roman-numerals.html
		StringBuffer romanNumeralStr = new StringBuffer();
		int remainder = positiveInteger;
		RomanNumeral[] numeralsArr = RomanNumeral.values();
		Queue<RomanNumeral> numerals = new LinkedList<RomanNumeral>();
		Collections.addAll(numerals, numeralsArr);
		while (numerals.size() > 0) {
			for (int i = 0; i < numerals.size(); i++) {
				RomanNumeral n = numerals.remove();
				int factor = remainder / n.value;
				if (factor >= 1) {
					for (int f = factor; f > 0; f--) {
						romanNumeralStr.append(n);
					}
					// Get the remainder
					remainder = remainder % n.value;
					if (remainder == 0)
						return romanNumeralStr.toString();
				}
			}
		}

		return romanNumeralStr.toString();
	}
}

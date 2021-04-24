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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class RomanNumeralConverterTest {
	private static final String ROMANNUMERAL_TEST_DATA_FILE = "romannumeraltestdata.txt";
	
	/**
	 * Check conversion of a broad set of example values using {@ROMANNUMERAL_TEST_DATA_FILE}.
	 * @throws InputOutOfSupportedRangeException
	 */
    @Test
	public void testValues() throws InputOutOfSupportedRangeException {
		// Read the test data file and check
		URL testDataFile = RomanNumeralConverter.class.getResource(ROMANNUMERAL_TEST_DATA_FILE);
		// Make sure the test data file exists
		assertNotNull(testDataFile);
		try {
			// Read the file
			try (InputStreamReader streamReader = new InputStreamReader(testDataFile.openStream(),
					StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(streamReader)) {
				String line;
				while ((line = reader.readLine()) != null) {
					if (line != null) {
						// The file is in tab delimited format - split by tab
						String[] intRomanNumeral = line.split("\t");
						if (intRomanNumeral.length == 2) {
							int inputInt = Integer.parseInt(intRomanNumeral[0]);
							if(inputInt >= 1 && inputInt <= 3999) {
								assertEquals(RomanNumeralConverter.convertToRomanNumeral(inputInt), intRomanNumeral[1], "Input: " + inputInt);
							} else {
								assertThrows(InputOutOfSupportedRangeException.class, () -> {
									RomanNumeralConverter.convertToRomanNumeral(inputInt);
								});
							}
						}
					}
				}
			}
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
	}
}

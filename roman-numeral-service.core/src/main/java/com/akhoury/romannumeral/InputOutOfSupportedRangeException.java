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

/**
 * Thrown to indicate that the application has attempted to convert an number that is not an integer in the range 1-3999.
 */
public class InputOutOfSupportedRangeException extends Exception {
	public InputOutOfSupportedRangeException(String inputInt) {
        super("Invalid input value " + inputInt + ". Input must be an integer in the range 1-3999");
    }
}

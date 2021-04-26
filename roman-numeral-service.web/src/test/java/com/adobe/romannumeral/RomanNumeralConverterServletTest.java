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
package com.adobe.romannumeral;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.servlethelpers.MockRequestPathInfo;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * Test the RomanNumeralConverterServlet.
 */
public class RomanNumeralConverterServletTest {
	private static final String HTTP_METHOD_GET = "GET";
	private static final String REQUEST_PATH = "/romannumeral";
	
	@InjectMocks
	public RomanNumeralConverterServlet servlet = new RomanNumeralConverterServlet();

	@BeforeEach 
	public void setUp() throws LoginException {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test that the servlet responds with 422 response when there invalid or missing input.
	 * @throws IOException 
	 */
	@Test
	public void testInvalidRequest() throws IOException {
		MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(null);
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		requestPathInfo.setResourcePath(REQUEST_PATH);
		request.setMethod(HTTP_METHOD_GET);
		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

		// Test with invalid querystring parameter value
		request.setQueryString("query=ab");
		servlet.doGet(request, response);
		assertEquals(422, response.getStatus());

		// Test with an empty querystring
		request.setQueryString("");
		servlet.doGet(request, response);
		assertEquals(422, response.getStatus());
	}

	/**
	 * Test when the input is out of the integer range 1-3999.
	 * @throws IOException
	 */
	@Test
	public void testOutOfRangeRequest() throws IOException {
		MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(null);
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		requestPathInfo.setResourcePath(REQUEST_PATH);
		request.setMethod(HTTP_METHOD_GET);
		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();
		
		// Test with out of range input
		request.setQueryString("query=5000");
		servlet.doGet(request, response);

		assertEquals(422, response.getStatus());
	}
	
	/**
	 * Test a valid input value of 100.
	 * @throws IOException
	 */
	@Test
	public void test100() throws IOException {
		MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(null);
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		requestPathInfo.setResourcePath(REQUEST_PATH);
		request.setMethod(HTTP_METHOD_GET);
		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

		// Test valid input value of 100.
		request.setQueryString("query=100");
		servlet.doGet(request, response);

		// Validate the Content-Type response header and the response body
		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals("{\"input\":\"100\",\"output\":\"C\"}", response.getOutputAsString());
	}
}
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

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.servlethelpers.MockRequestPathInfo;
import org.apache.sling.servlethelpers.MockSlingHttpServletRequest;
import org.apache.sling.servlethelpers.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class RomanNumeralServletTest {
	private static final String REQUEST_PATH = "/romannumeral";
	@InjectMocks
	public RomanNumeralConverterServlet servlet = new RomanNumeralConverterServlet();

	@BeforeEach 
	public void setUp() throws LoginException {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testInvalidRequest() throws Exception {
		MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(null);
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		requestPathInfo.setResourcePath(REQUEST_PATH);
		request.setMethod("GET");
		request.setQueryString("query=ab");

		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

		servlet.doGet(request, response);

		assertEquals(422, response.getStatus());
	}

	@Test
	public void testOutOfRangeRequest() throws Exception {
		MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(null);
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		requestPathInfo.setResourcePath(REQUEST_PATH);
		request.setMethod("GET");
		request.setQueryString("query=5000");

		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

		servlet.doGet(request, response);

		assertEquals(422, response.getStatus());
	}
	
	@Test
	public void test100() throws Exception {
		MockSlingHttpServletRequest request = new MockSlingHttpServletRequest(null);
		MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
		requestPathInfo.setResourcePath(REQUEST_PATH);
		request.setMethod("GET");
		request.setQueryString("query=100");

		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

		servlet.doGet(request, response);

		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals("{\"input\":\"100\",\"output\":\"C\"}", response.getOutputAsString());
	}
}
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

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.akhoury.romannumeral.InputOutOfSupportedRangeException;
import com.akhoury.romannumeral.RomanNumeralConverter;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Roman Numeral Converter Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/romannumeral" })
public class RomanNumeralConverterServlet extends SlingSafeMethodsServlet {

	private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
	private static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain;charset=UTF-8";
	private static final int HTTP_422_STATUS_UNPROCESSABLE_ENTITY = 422;
	private static final String INVALID_QUERY_PARAM_MESSAGE = "query parameter must be an integer in the range 1-3999.";
	private static final String PARAM_QUERY = "query";

	/**
	 * Servlet takes parameter "query" as an integer.
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		String queryParam = request.getParameter(PARAM_QUERY);
		int inputInt = -1;
		try {
			inputInt = Integer.parseInt(queryParam);
			//Output JSON on valid requests
			response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
			response.getWriter().write("{\"input\":\"" + inputInt + "\",\"output\":\""
					+ RomanNumeralConverter.convertToRomanNumeral(inputInt) + "\"}");
		} catch (NumberFormatException e) {
			response.setContentType(TEXT_PLAIN_CHARSET_UTF_8);
			response.setStatus(HTTP_422_STATUS_UNPROCESSABLE_ENTITY);
			response.getWriter().write(INVALID_QUERY_PARAM_MESSAGE);
		} catch (InputOutOfSupportedRangeException e) {
			response.setContentType(TEXT_PLAIN_CHARSET_UTF_8);
			response.setStatus(HTTP_422_STATUS_UNPROCESSABLE_ENTITY);
			response.getWriter().write(INVALID_QUERY_PARAM_MESSAGE);
		} finally {
			response.flushBuffer();
		}
	}
}
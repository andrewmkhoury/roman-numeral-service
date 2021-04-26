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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akhoury.romannumeral.InputOutOfSupportedRangeException;
import com.akhoury.romannumeral.RomanNumeralConverter;

/**
 * This servlet is a simple web service for converting integers in range 1-3999 to Roman Numeral.
 * It accepts GET requests to /romannumeral with parameter "query".  The "query" parameter must be
 * an integer in the range 1-3999.
 * 
 * Example request:
 * http://localhost:8080/romannumeral?query=1 
 * 
 * Results in this output:
 * {"input":"1","output":"I"}
 * 
 * Invalid or missing input results in 422 Unprocessable Entity status response and message {@value INVALID_QUERY_PARAM_MESSAGE}
 */
@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Roman Numeral Converter Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.paths=" + "/romannumeral" })
public class RomanNumeralConverterServlet extends SlingSafeMethodsServlet {

	private final static Logger log = LoggerFactory.getLogger(RomanNumeralConverter.class);

	private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
	private static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain;charset=UTF-8";
	private static final int HTTP_422_STATUS_UNPROCESSABLE_ENTITY = 422;
	private static final String INVALID_QUERY_PARAM_MESSAGE = "\"query\" parameter must be an integer in the range 1-3999.";
	private static final String PARAM_QUERY = "query";

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		String queryParam = request.getParameter(PARAM_QUERY);
		int inputInt = -1;
		if(queryParam != null) {
			try {
				inputInt = Integer.parseInt(queryParam);
				String romanNumeral = RomanNumeralConverter.convertToRomanNumeral(inputInt);
				if(log.isTraceEnabled()) {
					log.trace("Converted input {} to Roman Numeral {}", inputInt, romanNumeral);
				}
				// Output JSON on valid requests
				response.setContentType(APPLICATION_JSON_CHARSET_UTF_8);
				response.getWriter().write("{\"input\":\"" + inputInt + "\",\"output\":\""
						+ romanNumeral + "\"}");
			} catch (NumberFormatException e) {
				sendErrorResponse(response);
				if(log.isTraceEnabled()) {
					log.trace("Error while parsing query parameter input", e);
				}
			} catch (InputOutOfSupportedRangeException e) {
				sendErrorResponse(response);
				if(log.isTraceEnabled()) {
					log.trace("Input integer {} is outside of supported range 1-3999", inputInt);
				}
			} finally {
				response.flushBuffer();
			}
		} else {
			sendErrorResponse(response);
			log.trace("\"query\" querystring parameter is missing");
		}
	}

	private void sendErrorResponse(SlingHttpServletResponse response) throws IOException {
		response.setContentType(TEXT_PLAIN_CHARSET_UTF_8);
		response.setStatus(HTTP_422_STATUS_UNPROCESSABLE_ENTITY);
		response.getWriter().write(INVALID_QUERY_PARAM_MESSAGE);
	}
}
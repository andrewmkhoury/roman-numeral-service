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
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/romannumeral"
		})
public class RomanNumeralConverterServlet extends SlingSafeMethodsServlet {

	private static final String PARAM_QUERY = "query";

	/**
	 * Servlet takes parameter "query" as an integer.
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		String queryParam = request.getParameter(PARAM_QUERY);
		int inputInt = Integer.parseInt(queryParam);
		try {
			response.getWriter().write("{\"input\":\"" + inputInt + "\", \"output\":" + RomanNumeralConverter.convertToRomanNumeral(inputInt) + "}");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InputOutOfSupportedRangeException e) {
			e.printStackTrace();
		} finally {
			response.flushBuffer();
		}
	}
}
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

	private static final String SERVLET_PATH = "/bin/acs-commons/jcr-compare";

	private static final String SERVLET_SELECTORS = "hashes";

	private static final String SERVLET_EXTENSION = "txt";

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
		requestPathInfo.setSelectorString(SERVLET_SELECTORS);
		requestPathInfo.setExtension(SERVLET_EXTENSION);
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
		requestPathInfo.setSelectorString(SERVLET_SELECTORS);
		requestPathInfo.setExtension(SERVLET_EXTENSION);
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
		requestPathInfo.setSelectorString(SERVLET_SELECTORS);
		requestPathInfo.setExtension(SERVLET_EXTENSION);
		request.setMethod("GET");
		request.setQueryString("query=100");

		MockSlingHttpServletResponse response = new MockSlingHttpServletResponse();

		servlet.doGet(request, response);

		assertEquals("application/json;charset=UTF-8", response.getContentType());
		assertEquals("{\"input\":\"100\",\"output\":\"C\"}", response.getOutputAsString());
	}
}
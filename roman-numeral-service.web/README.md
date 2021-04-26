# Web
This project implements an OSGi bundle containing the following:
1. A web service for simple conversion from integer in the range 1-3999 to a Roman Numeral string contained in JSON.  See [here](https://github.com/andrewmkhoury/roman-numeral-service#web-service-specification) for more details.
2. Integration of the server with Prometheus monitoring of the JVM and Apache Sling.

# Unit Testing
There is [one unit test class](https://github.com/andrewmkhoury/roman-numeral-service/blob/master/roman-numeral-service.web/src/test/java/com/adobe/romannumeral/RomanNumeralConverterServletTest.java) that tests the web service ensuring proper output.

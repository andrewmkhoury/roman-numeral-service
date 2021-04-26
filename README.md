# Roman Numeral Service

This project implements a simple web service that takes an integer in the range 1-3999 and converts it to a Roman Numeral string.

The Roman Numeral conversion adheres to the specification on this web site: [https://www.mathsisfun.com/roman-numerals.html](https://www.mathsisfun.com/roman-numerals.html).

## Web Service Specification
The web service HTTP end-point under ``/romannumeral`` accepts a querystring parameter ``query`` with the integer (in range 1-3999) to be converted.

	http://localhost:8080/romannumeral?query={integer}

``{integer}`` must be any integer value in the range 1-3999.

Properly formed requests with ``{integer}`` in the correct range will receive an HTTP 200 (OK) response with JSON containing the result.

For example, this request ``http://localhost:8080/romannumeral?query=1`` would result in this output:
	
	{"input":"1","output":"I"}

Invalid (or out of range) input will receive a 422 (Uprocessable Entity) response with:

	"query" parameter must be an integer in the range 1-3999.


## Modules

The modules of the maven project are:

* core: Java bundle containing the core code which implements the Roman Numeral conversion.
* web: Servlet implementing the web service.
* it.tests: Java based integration tests

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To deploy the bundles run this command:

    mvn clean install sling:install

## Testing

There are two levels of testing contained in the project:

### Unit tests

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean test

### Integration tests

This allows running integration tests that exercise the capabilities of Sling via
HTTP calls to its API. To run the integration tests, run:

    mvn clean verify -Plocal

Test classes must be saved in the `src/main/java` directory (or any of its
sub-directories), and must be contained in files matching the pattern `*IT.java`.

The configuration provides sensible defaults for a typical local installation of
AEM. If you want to point the integration tests to different AEM author and
publish instances, you can use the following system properties via Maven's `-D`
flag.

| Property | Description | Default value |
| --- | --- | --- |
| `it.sling.url` | URL of the sling instance | `http://localhost:8080` |
| `it.sling.user` | Admin user for the sling instance | `admin` |
| `it.sling.password` | Password of the admin user for the sling instance | `admin` |

The integration tests in this archetype use the [AEM Testing
Clients](https://github.com/adobe/aem-testing-clients) and showcase some
recommended [best
practices](https://github.com/adobe/aem-testing-clients/wiki/Best-practices) to
be put in use when writing integration tests for AEM.


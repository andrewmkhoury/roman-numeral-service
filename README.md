# Roman Numeral Service

The Roman Numeral Service is a web service that takes a positive integer in the range 1-3999 and converts it to Roman Numeral.

## Modules

The main parts of the template are:

* core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
* web: Java based integration tests
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
subdirectories), and must be contained in files matching the pattern `*IT.java`.

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


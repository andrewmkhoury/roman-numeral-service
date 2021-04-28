# Roman Numeral Service

This project implements a simple web service that takes an integer in the range 1-3999 and converts it to a Roman Numeral string.

The Roman Numeral conversion adheres to the specification on this web site: [https://www.mathsisfun.com/roman-numerals.html](https://www.mathsisfun.com/roman-numerals.html).

For a very detailed explanation of the engineering process involved in this project, see [engineering process documentation](engineering-process.md).

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

* [core](roman-numeral-service.core): Java bundle containing the core code which implements the Roman Numeral conversion.
* [web](roman-numeral-service.web): Servlet implementing the `/romannumeral` web service.  It also includes an OSGi component that registers the Prometheus `/metrics` servlet.

## Build the Code
1. Install Java 11 (Open JDK/JRE or Oracle JDK/JRE): https://openjdk.java.net/install/
2. Download and install Maven 3.6.3 (if not preinstalled): https://downloads.apache.org/maven/maven-3/3.6.3/binaries/
   * Installation instructions can be found here: https://maven.apache.org/install.html
4. To build all the modules, run this command in the project root directory (with Maven 3.6.3):

       mvn clean install

NOTE: The web framework used by this project is Apache Sling.  This was mainly to demonstrate my working knowledge of how to develop with it.  See [here for more information on this decision](engineering-process.md#web-framework).

### Installing Apache Sling Locally

1. Download and install Java 11 (OpenJDK or Oracle JDK): https://openjdk.java.net/install/
2. Download the Apache Sling standalone jar file to a new directory:

       wget https://repo1.maven.org/maven2/org/apache/sling/org.apache.sling.starter/11/org.apache.sling.starter-11.jar

3. Start and install Apache Sling:
       
       java -Xmx512m -jar org.apache.sling.starter-11.jar
       
4. Open the installed Sling server in your browser [http://localhost:8080](http://localhost:8080)

### Deploy the Application to Apache Sling

1. To build and deploy the OSGi bundles to an already running sling instance on http://localhost:8080, run this command:

       mvn clean install sling:install

2. Once the bundles are installed then go to http://localhost:8080/romannumeral?query=5 to see the Roman Numeral web service.
3. Go to this URL to see the Prometheus monitoring metrics web end-point: http://localhost:8080/metrics

## Build and Deploy on Docker / Kubernetes
The application and its monitoring and observability services (Prometheus & Grafana) can be deployed to a Kubernetes cluster.  Follow the steps below to build and deploy to Kubernetes.

1. Install Docker if it isn't already installed https://docs.docker.com/get-docker/
2. If you have another version of Kubernetes pre-installed (such as minikube) then stop those services so the docker desktop one can take over. 
3. Enable single node Kubernetes via Docker Desktop (Settings UI): https://docs.docker.com/desktop/kubernetes/#enable-kubernetes
4. Clone this git repository:
     
       git clone https://github.com/andrewmkhoury/roman-numeral-service.git
	
5. Download and install Maven 3.6.3 / JDK 11: https://maven.apache.org/install.html
	* JDK 11: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
	* Maven: https://maven.apache.org/download.cgi

6. Run the maven command from the root of the project directory to build all modules:
	
		mvn clean install
	
7. Build the 3 docker containers (I purposely didn't publish them to docker hub (hence the detail in step 2 and 3 about using docker desktop's kubernetes):

		docker build -t roman-numeral-service:1.0 .
		docker build -t prometheus-roman-services:1.0 ./prometheus
		docker build -t grafana-roman-services:1.0 ./grafana
	
8. Deploy the Docker images via Kubernetes:

		kubectl apply -f kube-deployment.yaml
	
9. Now you have 3 servers running and exposed on your machine:
	1. Apache Sling (Web): http://localhost:8080
		* Roman Numeral Web Service: [http://localhost:8080/romannumeral?query=1](http://localhost:8080/romannumeral?query=1)
		* Prometheus Metrics endpoint: [http://localhost:8080/metrics](http://localhost:8080/metrics)
	2. Prometheus (Monitoring): http://localhost:9090
		* [http://localhost:9090](http://localhost:9090)
	3. Grafana (Observability): http://localhost:3000
		* User / password is admin / admin (default)
		* Seee the dashboards here: http://localhost:3000/dashboards - there is a JVM monitoring dashboard
		
	NOTE: The Apache Sling container can take up to 2 minutes to start completely before the /romannumeral web service is accessible.

10. Now test the Web Service by visiting this URL in your browser: [http://localhost:8080/romannumeral?query=3](http://localhost:8080/romannumeral?query=3)


## Testing

There are two levels of testing contained in the project:

### Unit tests

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean test

### Java CI: Github Action
A GitHub Action is enabled to run the build / tests on-commit to git:
[.github/workflows/maven.yml](https://github.com/andrewmkhoury/roman-numeral-service/blob/master/.github/workflows/maven.yml)


## Dependency Attribution
To generate detailed information about all dependencies run this maven command:
	
	mvn attribution:generate-attribution-file

It generates a `attribution.xml` under each project's target directory for the maven dependencies.

This feature uses the attribution-maven-plugin: https://github.com/jinnovations/attribution-maven-plugin.

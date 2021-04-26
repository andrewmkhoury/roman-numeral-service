# Overview
This document lays out the engineering process behind the implementation of the Roman Numeral Service application.

# Project Decisions
This section explains the rationale behind my decision making on the implementation, project structure and technologies selected.

## Build
Due to the selection of Java as the language, the logical choice for build management is Apache Maven.

## Roman Numeral Converter
1. Studied Roman Numerals [https://www.mathsisfun.com/roman-numerals.html](https://www.mathsisfun.com/roman-numerals.html)
2. Created a simple java program that implements the Roman Numeral conversion.
   * Due to the small numerical range of 1-3999, the most efficient solution would be to generate a static array containing all predefined String values of all Roman Numerals in the range 1-3999.  However, I understand that wasn't the purpose of the test, so I instead implemented an algorithm that generates the Roman Numerals dynamically.
   * Implemented as a static method as it needn't maintain any object state.  Testing is easier and cleaner in this case with a simple static method implementation.  This is similar to the [java.lang.Math](https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html) libraries.
   * For the sake of maintainability and readability, I copy the unique Roman Numeral values from an enum to a LIFO queue.
   * For the converter, a two array implementation (int array and String array) would have slightly better performance and use less memory, however the code is more maintainable using collections.
   * The algorithm copies the enum values to a new Queue (LinkedList), then iterates, dequeuing from highest to lowest unique Roman Numeral value.  For each iteration, it divides, searching for the first value that yields a non-zero quotient.  Then it takes the remainder at each pass and repeats until the queue is empty.

## Web framework
I chose Apache Sling as the web framework for the application to demonstrate my knowledge of the inner workings of AEM and proficiency in implementing an application dealing with Apache Felix / OSGi, Sling, etc.  

*NOTE:* If I were to implement such a simple web service in a real world scenario, I would use something more lightweight like Jetty servlet engine or using a simple Azure Function https://docs.microsoft.com/en-us/azure/azure-functions/functions-create-serverless-api.  The decision of which technology to choose would depend on the technology ecosystem surrounding it.

## Logging
Used standard `org.slf4j.Logger` common in Apache Felix and Sling applications.

Note: Since the logging was particularly for test demonstration purposes I made it all trace logging.  In a real world scenario, such a simple application would have minimal logging in the actual Servlet or library code.

## Testing
Unit tests are implemented for both OSGi bundles and surefire maven plugin is enabled.

Developing integration tests was out of scope as step 3 was open ended.  See [Integration Testing](#integration-testing) below for considerations on how this would have been done.

## Continuous Integration: Github Action
A GitHub Action is enabled to run the build / tests on-commit to git:
[.github/workflows/maven.yml](https://github.com/andrewmkhoury/roman-numeral-service/blob/master/.github/workflows/maven.yml)

See here [https://github.com/andrewmkhoury/roman-numeral-service/actions](https://github.com/andrewmkhoury/roman-numeral-service/actions)

## DevOps Features

### Container Technologies
Per the test recommendation and what is in use by Adobe, I selected Docker as the container technology and Kubernetes for container orchestration.

### Monitoring & Observability
Prometheus and Grafana chosen due to popularity and since they are in use at Adobe.

# Maven / Java Project Creation Steps
I generated the initial project structure using the Sling Maven Archetypes:

1. Used the sling maven archetypes
	* Parent pom:
		
			mvn archetype:generate \
			  -DarchetypeGroupId=org.codehaus.mojo.archetypes \
			  -DarchetypeArtifactId=pom-root \
			  -DarchetypeVersion=RELEASE \
			  -DgroupId=com.akhoury \
			  -DartifactId=sling-multi-module-maven-project \
			  -Dversion=1.0.0-SNAPSHOT \
			  -DinteractiveMode=false
			  
	* Modules:
		
			mvn archetype:generate \
			  -DarchetypeGroupId=org.apache.sling \
			  -DarchetypeArtifactId=sling-bundle-archetype \
			  -DgroupId=com.akhoury \
			  -DartifactId=roman-numeral.core\
			  -Dversion=1.0.0-SNAPSHOT \
			  -Dpackage=com.akhoury.romannumeral \
			  -DappsFolderName=project \
			  -DartifactName="core" \
			  -DpackageGroup="core" \
			  -DinteractiveMode=false

			mvn archetype:generate \
			  -DarchetypeGroupId=org.apache.sling \
			  -DarchetypeArtifactId=sling-bundle-archetype \
			  -DgroupId=com.akhoury \
			  -DartifactId=roman-numeral-service.web \
			  -Dversion=1.0.0-SNAPSHOT \
			  -Dpackage=com.adobe.romannumeral \
			  -DappsFolderName=project \
			  -DartifactName="web" \
			  -DpackageGroup="web" \
			  -DinteractiveMode=false

2. Created a [.gitignore](.gitignore) file
3. Added the project to git

		git init
		git add .
		git add --all
		git commit -m 'Initial project'
		git remote add origin https://github.com/andrewmkhoury/roman-numeral-service.git
		git remote -v
		git push origin master

3. [Formatted the pom files and added more recent test dependencies, upgrading to junit5](https://github.com/andrewmkhoury/roman-numeral-service/commit/80c8acddeee80f6e59c02f6356df06a9390542ac):
	* [Parent pom](https://github.com/andrewmkhoury/roman-numeral-service/blob/9eeb73083999f2f64e0ce8bf9cf1cf106b95cbb5/pom.xml#L80)
	* [Core module](https://github.com/andrewmkhoury/roman-numeral-service/blob/9eeb73083999f2f64e0ce8bf9cf1cf106b95cbb5/roman-numeral-service.core/pom.xml#L117)
	* [Web module](https://github.com/andrewmkhoury/roman-numeral-service/blob/master/roman-numeral-service.web/pom.xml#L122)

4. Implemented JUnit 5 test cases:
	* [core RomanNumeralConverter test case](https://github.com/andrewmkhoury/roman-numeral-service/commit/6a113031e6a0528e4a82ccaa57447aa750e707ee)
	* [web Servlet cases using mock objects](https://github.com/andrewmkhoury/roman-numeral-service/commit/6a113031e6a0528e4a82ccaa57447aa750e707ee)
	* Favored [wcm.io library](https://wcm.io/testing/aem-mock/usage.html) over the [sling testing library](https://github.com/apache/sling-org-apache-sling-commons-testing).  The wcm.io library is far more feature rich especially the [org.apache.sling.commons.testing.sling.MockSlingHttpServletRequest](https://sling.apache.org/apidocs/sling6/org/apache/sling/commons/testing/sling/MockSlingHttpServletRequest.html).


# Containerization

## Dockerizing: Apache Sling / Java Application

1. Created [Dockerfile](Dockerfile) and [.dockerignore](.dockerignore) and ran docker commands:
	
	docker build -t roman-numeral-service:1.0 .
	docker run -d -p 8080:8080 --name roman-numeral-service roman-numeral-service
	docker exec -it roman-numeral-service /bin/sh

2. Test the container and stop it in prep for kubernetes:
	docker stop roman-numeral-service; docker rm roman-numeral-service

# Monitoring & Observability

Due to popularity (and what is in use at Adobe), I selected Prometheus as the monitoring server and Grafana for observability.

## Monitoring: Prometheus
Integrate monitoring using prometheus
* https://github.com/prometheus/client_java
* https://www.robustperception.io/exposing-dropwizard-metrics-to-prometheus

1. Created [Dockerfile](prometheus/Dockerfile) for Prometheus server. 

2. Built the image and tested it running directly.

	docker build -t prometheus-roman-services:1.0 .
	docker run -d -p 9090:9090 --name prometheus-roman-services prometheus-roman-services
	docker exec -it prometheus-roman-services /bin/sh

3. Test the container and stop it in prep for kubernetes:

	docker stop prometheus-roman-services; docker rm prometheus-roman-services

## Observability: Grafana
Create a docker container for Grafana.
	
1. Created [Dockerfile](prometheus/Dockerfile) for Prometheus server. 

2. Built the image and tested it running directly.

	docker build -t grafana-roman-services:1.0 ./grafana
	docker run -d -p 9090:9090 --name grafana-roman-services grafana-roman-services
	docker exec -it grafana-roman-services /bin/sh

3. Test the container and stop it in prep for kubernetes:

	docker stop grafana-roman-services; docker rm grafana-roman-services

4. Followed this guide to create [grafana/provisioning](grafana/provisioning) directory structure: https://blog.56k.cloud/provisioning-grafana-datasources-and-dashboards-automagically/

5. Download the JSON of this dashboard to [grafana/provisioning/dashboards](grafana/provisioning/dashboards): https://grafana.com/grafana/dashboards/3066

# Kubernetes

1. Installed Kubernetes to manage the docker network.

	brew install minikube
	minikube start
	kubectl cluster-info
	kubectl get nodes
	minikube dashboard #opens kubenetes UI in browser
	
2. Create a [yaml file](kube-deployment.yaml) to define a kubernetes deployment.  The file references the 3 docker images and exposes all the ports for Sling (port 8080), Prometheus (9090), and Grafana (3000).  It defines to have only one replica in the replica set as this is for testing and development of the monitoring.

3. Apply the yaml to the kubernetes cluster:

	kubectl apply -f kube-deployment.yaml

## Monitoring Deployment Considerations

In a real life scenario, the monitoring systems would be in a separate Kubernetes cluster.  However,

## Monitoring the Kubernetes Cluster

Due to the time constraints of the test, the monitoring of the kubernetes cluster itself was out of scope.

However, the steps to implement it would be as follows:

1. Configure Kubernetes and Prometheus for monitoring the Kubernetes cluster: https://phoenixnap.com/kb/prometheus-kubernetes-monitoring
2. Dowload and add this dashboard to [grafana/provisioning/dashboards](grafana/provisioning/dashboards): https://grafana.com/grafana/dashboards/315


# Build Pipeline
https://codefresh.io/docker-tutorial/java_docker_pipeline/

# Architecture
I created a development environment demonstrating a full development deploy including monitoring with prometheus and charts with grafana.  That was for demo purposes only.

In a real life production scenario, the prometheus and grafana servers would have their own separate kubernetes deployment.


# Out of Scope features

### Integration Testing
I had intended to implement integration tests, but ran out of time as extension 3 of the test is quite open ended.

If I had the time to implement the it.tests, I would have followed the newer Sling Test libraries for JUnit5:
https://sling.apache.org/documentation/bundles/org-apache-sling-junit-bundles.html

Under the project directory I would have added roman-numeral-service.it.tests/pom.xml and added its module to the parent project [pom.xml](pom.xml#L25).

## Security

### Passwords / Secrets
Since the time for this test was limited, application security was out of scope.

However, to implement the security:
1. Password/Secret Management
	* The admin passwords (for sling, prometheus and grafana) would be configurable via kubernetes secret feature: https://kubernetes.io/docs/tasks/inject-data-application/distribute-credentials-secure/

2. Apache Sling
	* Implement an OSGi bundle with Activator class that reads the password once and sets the admin password (and any other out of the box users passwords).
	* Code for changing the password would be similar to this: https://stackoverflow.com/questions/5969500/how-to-change-the-admin-password-in-jackrabbit

3. Grafana
	* The password would get passed in and set via grafana-cli command (in the [Dockerfile](grafana/Dockerfile)): https://grafana.com/docs/grafana/latest/administration/cli/#reset-admin-password

4. Prometheus
	* add an nginx Docker image as a reverse proxy per this documentation to secure prometheus server as well: https://prometheus.io/docs/guides/basic-auth
	* Set the https://prometheus.io/docs/guides/basic-auth/#nginx-configuration

		http {
			server {
				listen 12321;
				location /prometheus/ {
					auth_basic           "Prometheus";
					auth_basic_user_file /etc/nginx/.htpasswd;
					proxy_pass           http://localhost:9090/;
				}
			}
		}
		events {}

### Security Testing
#### Docker
In a normal scenario we would run `docker scan` to scan the container security.

#### Web Security
For web security we might use something like burpsuite.  For example: https://netsoss.github.io/headless-burp/user-guide/maven-plugin/usage/

### HTTPS / TLS
Another obvious thing to do would be to enable HTTPS (TLS) for all of the web ports of these applications.  This documented:
* Sling: Enable SSL at the Sling level (steps documented here for AEM6.1 work for sling http://www.aemcq5tutorials.com/tutorials/enable-https-aem/) and/or enable it at the Load Balancer.
* Prometheus: https://prometheus.io/docs/guides/tls-encryption/
* Grafana: https://stackoverflow.com/questions/39956790/grafana-switch-from-http-to-https

### Sample Content and Default Settings
Another consideration is to remove all sample content from Apache Sling and lock down the "Sling Authentication Service" settings.  However, as I stated earlier, in a real world implementation, we would implement such a simple web service with something more lightweight like Jetty or even just an Azure Function.

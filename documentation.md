
How the project structure was generated:

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
	
5. 

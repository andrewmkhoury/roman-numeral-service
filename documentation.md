How the project structure was generated:

Parent pom:
```
mvn archetype:generate \
  -DarchetypeGroupId=org.codehaus.mojo.archetypes \
  -DarchetypeArtifactId=pom-root \
  -DarchetypeVersion=RELEASE \
  -DgroupId=com.akhoury \
  -DartifactId=sling-multi-module-maven-project \
  -Dversion=1.0.0-SNAPSHOT \
  -DinteractiveMode=false
```

Modules:
```
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
```

```
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
```

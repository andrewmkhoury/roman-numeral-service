FROM apache/sling:11

RUN mvn install

COPY ./*/target/roman-numeral-service*.jar /opt/sling/sling/install 

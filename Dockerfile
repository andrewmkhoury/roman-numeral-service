FROM apache/sling:11
ENV JAVA_OPTS -Xmx512m -Dsling.fileinstall.dir=/opt/sling/sling/install

COPY ./roman-numeral-service.core/target/roman-numeral-service*.jar /opt/sling/sling/install/
COPY ./roman-numeral-service.web/target/roman-numeral-service*.jar /opt/sling/sling/install/

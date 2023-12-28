FROM amazoncorretto:21
VOLUME /tmp
COPY target/basic-security-demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
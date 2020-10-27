FROM adoptopenjdk/openjdk11:x86_64-alpine-jre11u-nightly
COPY target/*.jar app.jar
ENTRYPOINT ["sh","-c","java -jar /app.jar"]
EXPOSE 8080
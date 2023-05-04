FROM openjdk:19-jdk-alpine
COPY build/libs/TuiTask-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
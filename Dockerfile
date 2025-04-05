FROM eclipse-temurin:21-jdk-jammy
LABEL authors="marko"
WORKDIR /myapp

COPY build/libs/*-SNAPSHOT.jar /myapp/myapp.jar

ENTRYPOINT ["java", "-jar", "/myapp/myapp.jar"]

# built with docker build -t myapp_image .
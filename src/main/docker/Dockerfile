FROM openjdk:8-jre-alpine

ADD target/*.jar websocket.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar","/websocket.jar"]


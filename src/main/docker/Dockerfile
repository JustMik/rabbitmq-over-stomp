FROM openjdk:8-jre-alpine

ADD target/*.jar websocket.jar
ENTRYPOINT ["java", "-Dserver.port=8090", "-Dnode.port=8090", "-Djava.security.egd=file:/dev/./urandom", "-jar","/websocket.jar"]



mvn clean package -DskipTests
cp target/*.jar src/main/docker/target/websocket.jar
docker-compose -f src/main/docker/app.yml build
docker-compose -f src/main/docker/app.yml up
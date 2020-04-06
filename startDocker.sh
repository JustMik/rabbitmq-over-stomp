
./mvnw spring-boot:build-image -DskipTests
docker-compose -f src/main/docker/app.yml build
docker-compose -f src/main/docker/app.yml up

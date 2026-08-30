FROM gradle:8.10-jdk21 AS build
WORKDIR /app

COPY settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
COPY common ./common
COPY producer ./producer
COPY consumer ./consumer


RUN ./gradlew :consumer:shadowJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/consumer/build/libs/*-all.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
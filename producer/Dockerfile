# ---- Stage 1: Build ----
FROM gradle:8.10-jdk21 AS build
WORKDIR /app

COPY settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
COPY common ./common
COPY producer ./producer
COPY consumer ./consumer


RUN ./gradlew :producer:shadowJar --no-daemon

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/producer/build/libs/*-all.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
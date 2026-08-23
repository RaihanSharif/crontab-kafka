# ---- Stage 1: Build ----
FROM gradle:8.10-jdk21 AS build

WORKDIR /app

# Copy Gradle config first so dependency resolution can be cached
# separately from source code changes.
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./

RUN ./gradlew dependencies --no-daemon || true

# Now copy the actual source and build the fat jar.
COPY src ./src

RUN ./gradlew shadowJar --no-daemon

# ---- Stage 2: Run ----
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy only the built jar from the build stage — nothing else
# from the build environment ends up in the final image.
COPY --from=build /app/build/libs/*-all.jar app.jar

# The crontab file is passed in as a mounted volume / arg at runtime,
# rather than baked into the image, so it stays flexible.
ENTRYPOINT ["java", "-jar", "app.jar"]
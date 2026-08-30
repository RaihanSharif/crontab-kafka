plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.cron.raihan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.quartz-scheduler:quartz:2.5.2")
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("org.apache.kafka:kafka-clients:4.1.2")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

application {
    mainClass.set("Main")
}
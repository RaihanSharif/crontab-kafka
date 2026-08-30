plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.raihan.cronkafka"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
dependencies {
    implementation(project(":common"))
    implementation("org.apache.kafka:kafka-clients:4.1.2") // match producer's version
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}
application {
    mainClass.set("org.raihan.cronkafka.consumer.ConsumerMain")
}
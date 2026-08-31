plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.raihan.cronkafka"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation("org.quartz-scheduler:quartz:2.5.2")
    implementation("org.apache.kafka:kafka-clients:4.1.2")
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.2")
}

application {
    mainClass.set("org.raihan.cronkafka.producer.Main")
}
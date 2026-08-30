package org.raihan.cronkafka.common;

public class KafkaConfig {
    public static String bootstrapServers() {
        return System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092");
    }
}
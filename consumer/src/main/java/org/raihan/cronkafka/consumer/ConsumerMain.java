package org.raihan.cronkafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.raihan.cronkafka.common.Job;
import org.raihan.cronkafka.common.KafkaConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerMain {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.bootstrapServers());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, System.getenv("KAFKA_GROUP_ID"));       // SAME group id for both instances
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        String topic = System.getenv("KAFKA_TOPIC");
        ObjectMapper mapper = new ObjectMapper();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of(topic));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        Job job = mapper.readValue(record.value(), Job.class);
                        System.out.println("Running job " + job.lineNum() + ": " + job.command());
                        runCommand(job);

                    } catch (JsonProcessingException e) {
                        System.err.println("Skipping malformed message at offset " + record.offset() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    private static void runCommand(Job job) {
        try {
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", job.command());
            pb.redirectErrorStream(true); // merge stderr into stdout so you see both in one stream
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[job " + job.lineNum() + "] " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Job " + job.lineNum() + " exited with code " + exitCode);

        } catch (IOException e) {
            System.err.println("Failed to start command for job " + job.lineNum() + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for job " + job.lineNum());
        }
    }
}

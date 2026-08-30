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

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ConsumerMain {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.bootstrapServers());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "job-consumers");       // SAME group id for both instances
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ObjectMapper mapper = new ObjectMapper();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("jobs"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, String> record : records) {
                    try {
                        Job job = mapper.readValue(record.value(), Job.class);
                        System.out.println("Running job " + job.lineNum());
                    } catch (JsonProcessingException e) {
                        System.err.println("Skipping malformed message at offset " + record.offset() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}

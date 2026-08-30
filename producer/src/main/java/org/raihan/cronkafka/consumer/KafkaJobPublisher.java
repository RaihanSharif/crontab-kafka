package org.raihan.cronkafka.consumer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Properties;
import java.util.UUID;
import org.raihan.cronkafka.common.Job;
import org.raihan.cronkafka.common.KafkaConfig;

public class KafkaJobPublisher implements org.quartz.Job {

    private static final Properties kafkaProps = buildProps();
    private static final KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProps);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static Properties buildProps() {
        Properties props = new Properties();
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.bootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return props;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        int lineNum = dataMap.getInt("lineNum");
        String command = dataMap.getString("command"); // pass this through from crontab parsing

        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, lineNum, command, Instant.now().toString());

        try {
            String json = mapper.writeValueAsString(job);
            ProducerRecord<String, String> record =
                    new ProducerRecord<>("jobs", jobId, json); // key = UUID → drives partition hash
            producer.send(record, (metadata, exception) -> {
                if (exception != null) exception.printStackTrace();
                else System.out.println("Published job " + jobId + " to partition " + metadata.partition());
            });
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }
}

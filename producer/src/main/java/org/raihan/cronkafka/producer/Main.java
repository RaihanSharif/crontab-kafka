package org.raihan.cronkafka.producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.raihan.cronkafka.common.KafkaConfig;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {

        // run with ./gradlew run --args="path/to/crontab.txt"
        List<String> lines = CrontabFileReader.read(args[0]);

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        SchedulerSetup.scheduleAll(scheduler, lines);

        Properties adminProps = new Properties();
        adminProps.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                KafkaConfig.bootstrapServers());

        try (AdminClient admin = AdminClient.create(adminProps)) {
            NewTopic topic = new NewTopic("jobs", 2, (short) 1);
            admin.createTopics(List.of(topic)).all().get();
            System.out.println("Created topic 'jobs'");
        } catch (ExecutionException e) {
            if (e.getCause() instanceof TopicExistsException) {
                System.out.println("Topic 'jobs' already exists, continuing");
            } else {
                throw e;
            }
        }
        scheduler.start();

    }

}

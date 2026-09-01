package org.raihan.cronkafka.producer;

public record CrontabEntry(
        String cronExpression,
        String command,
        String cluster
) {
    public CrontabEntry(String cronExpression, String command) {

        this(cronExpression, command, "default");
    }
}

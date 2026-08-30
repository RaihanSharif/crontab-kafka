package org.raihan.cronkafka.producer;

public record CrontabEntry(String cronExpression, String command) {}

package org.raihan.cronkafka.common;

public record Job(
        String jobId,        // UUID
        int lineNum,         // The line in the crontab file of this job
        String command,      // The command to be executed
        String scheduledTime  // ISO-8601 timestamp of intended run time
) {}
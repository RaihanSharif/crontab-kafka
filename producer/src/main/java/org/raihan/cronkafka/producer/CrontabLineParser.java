package org.raihan.cronkafka.producer;

import java.util.Arrays;

public class CrontabLineParser {

    public static CrontabEntry parse(String line) {
        // 5 cron fields + rest is command. Command may be space separated.
        String[] parts = line.trim().split("\\s+", 6);

        if (parts.length < 5) {
            throw new IllegalArgumentException(
                    "Expected at least 5 cron fields, got " + parts.length + ": " + line);
        }

        String cronPart = String.join(" ", Arrays.copyOfRange(parts, 0, 5));
        String command = parts.length > 5 ? parts[5] : "";

        return new CrontabEntry(cronPart, command);
    }
}

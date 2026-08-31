package org.raihan.cronkafka.producer;

import java.util.Arrays;

public class CrontabLineParser {

    public static CrontabEntry parse(String line) {
        String trimmed = line.trim();

        if (!trimmed.startsWith("@cluster")) {
            return parseClusteredLine(trimmed);
        }

        return parseLegacyCron(trimmed);
    }

    private static CrontabEntry parseLegacyCron(String line) {
        // 5 cron fields + rest is command. Command may be space separated.
        String[] parts = line.split("\\s+", 6);

        if (parts.length < 6) {
            throw new IllegalArgumentException(
                    "Expected at least 5 cron fields, got " + parts.length + ": " + line);
        }

        String cronPart = String.join(" ",
                Arrays.copyOfRange(parts, 0, 5));

        String command = parts[5];

        return new CrontabEntry(cronPart, command);
    }

    private static CrontabEntry parseClusteredLine(String line) {
        String[] parts = line.split("\\s+", 8);
        if (parts.length < 8) {
            throw new IllegalArgumentException(
                    "Expected @cluster + cluster + 5 cron fields + command: " + line);
        }

        String cluster = parts[1];
        String cronPart = String.join(" ",
                Arrays.copyOfRange(parts, 2, 7));

        String command = parts[7];

        return new CrontabEntry(cronPart, command, cluster);
    }
}

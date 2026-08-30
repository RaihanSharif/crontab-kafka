package org.raihan.cronkafka.consumer;

public class CronConverter {

    public static String toQuartzCron(String unixCron) {
        String[] fields = unixCron.trim().split("\\s+");

        if (fields.length != 5) {
            throw new IllegalArgumentException(
                    "Expected 5 fields in cron expression, got " + fields.length + ": " + unixCron);
        }

        String minute = fields[0];
        String hour = fields[1];
        String dayOfMonth = fields[2];
        String month = fields[3];
        String dayOfWeek = fields[4];

        if (dayOfMonth.equals("*") && dayOfWeek.equals("*")) {
            dayOfWeek = "?";
        } else if (dayOfMonth.equals("*")) {
            dayOfMonth = "?";
        } else if (dayOfWeek.equals("*")) {
            dayOfWeek = "?";
        }

        return String.join(" ", "0", minute, hour, dayOfMonth, month, dayOfWeek);
    }
}

package org.raihan.cronkafka.producer;

import org.quartz.*;

public class SchedulerSetup {

    public static void scheduleAll(Scheduler scheduler, Iterable<String> cronLines) throws SchedulerException {
        int jobNumber = 0; // file line number
        for (String line : cronLines) {
            CrontabEntry entry = CrontabLineParser.parse(line);
            scheduleOne(scheduler, jobNumber, entry);
            jobNumber++;
        }
    }

    private static void scheduleOne(Scheduler scheduler, int jobNumber, CrontabEntry entry) throws SchedulerException {
        String quartzCron = CronConverter.toQuartzCron(entry.cronExpression());

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger" + jobNumber, "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(quartzCron))
                .build();

        JobDetail job = JobBuilder.newJob(KafkaJobPublisher.class)
                .withIdentity("Job" + jobNumber, "group1")
                .usingJobData("lineNum", jobNumber)
                .usingJobData("command", entry.command())
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}

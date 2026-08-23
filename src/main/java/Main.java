import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        // run with ./gradlew run --args="path/to/crontab.txt"
        String path = args[0];
        System.out.println("hahahahaha");
        List<String> lines = Files.readAllLines(Path.of(path));

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        for (int i = 0; i < lines.size(); i++) {
            String cronLine = lines.get(i);

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger" + i, "group1")
                    .withSchedule(CronScheduleBuilder.cronSchedule(toQuartzCron(cronLine)))
                    .build();

            JobDetail job = JobBuilder.newJob(JobLogger.class)
                    .withIdentity("job" + i, "group1")
                    .usingJobData("lineNum", i)
                    .build();

            scheduler.scheduleJob(job, trigger);
        }

        scheduler.start();


    }

    private static String toQuartzCron(String unixCron) {
        String[] fields = unixCron.trim().split("\\s+");
        // fields: [minute, hour, dayOfMonth, month, dayOfWeek]
        String minute = fields[0];
        String hour = fields[1];
        String dayOfMonth = fields[2];
        String month = fields[3];
        String dayOfWeek = fields[4];

        // Quartz requires exactly one of dayOfMonth/dayOfWeek to be "?"
        if (dayOfMonth.equals("*") && dayOfWeek.equals("*")) {
            dayOfWeek = "?";
        }

        return String.join(" ", "0", minute, hour, dayOfMonth, month, dayOfWeek);
    }
}

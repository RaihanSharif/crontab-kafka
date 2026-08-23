import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        // run with ./gradlew run --args="path/to/crontab.txt"
        List<String> lines = CrontabFileReader.read(args[0]);

        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        SchedulerSetup.scheduleAll(scheduler, lines);
        scheduler.start();

    }

}

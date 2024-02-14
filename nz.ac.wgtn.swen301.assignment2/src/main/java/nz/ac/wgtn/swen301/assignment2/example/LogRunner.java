package nz.ac.wgtn.swen301.assignment2.example;

import nz.ac.wgtn.swen301.assignment2.MemAppender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LogRunner {
    public static void main(String[] args) throws InterruptedException {
        BasicConfigurator.configure();
        Logger logger = Logger.getLogger(LogRunner.class);
        Random random = new Random();
        MemAppender appender1 = new MemAppender();

        long startTime = System.currentTimeMillis();
        long endTime = startTime + TimeUnit.MINUTES.toMillis(2);

        MemAppender appender2 = new MemAppender();

        appender1.setName("Appender4");
        appender2.setName("Appender5");

        while (System.currentTimeMillis() < endTime) {
            Level[] levels = {Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR};
            Level randomLevel = levels[random.nextInt(levels.length)];
            String randomMessage = "Random log message #" + random.nextInt(1000);
            logger.log(randomLevel, randomMessage);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}

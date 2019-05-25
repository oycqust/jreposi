import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoggerBuilder2 {
 
    private static final Map<String, Logger> container = new HashMap<>();
    private static final String LOG_PATH="D:\\logtest";
    public Logger getLogger(String name) {
        Logger logger = container.get(name);
        if(logger != null) {
            return logger;
        }
        synchronized (LoggerBuilder2.class) {
            logger = container.get(name);
            if(logger != null) {
                return logger;
            }
            logger = build(name);
            container.put(name,logger);
        }
        return logger;
    }
 
    private static Logger build(String name) {
        DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
 
 
        Logger logger = context.getLogger("FILE-" + name);
        logger.setAdditive(false);
        RollingFileAppender appender = new RollingFileAppender();

        logger.setLevel(Level.INFO);
        appender.setContext(context);
        appender.setName("FILE-" + name);
        //appender.setFile(OptionHelper.substVars(LoggerBuilder.LOG_PATH + File.separator+ name + "_%d{yyyyMMdd}.log",context));
        appender.setAppend(true);
        appender.setPrudent(false);
        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
        //FixedWindowRollingPolicy policy = new FixedWindowRollingPolicy();
        String fp = OptionHelper.substVars(LoggerBuilder2.LOG_PATH + File.separator + name + "_%d{yyyyMMdd}.%i.log.zip",context);

        SizeBasedTriggeringPolicy sizeBasedTriggeringPolicy = new SizeBasedTriggeringPolicy();
        sizeBasedTriggeringPolicy.setMaxFileSize(FileSize.valueOf("1MB"));
        sizeBasedTriggeringPolicy.setContext(context);
        sizeBasedTriggeringPolicy.start();
        policy.setMaxFileSize(FileSize.valueOf("10MB"));
        //policy.setMinIndex(1);
        //policy.setMaxIndex(20);
        policy.setFileNamePattern(fp);
        policy.setMaxHistory(15);
        policy.setTotalSizeCap(FileSize.valueOf("32GB"));
        policy.setParent(appender);
        policy.setContext(context);

        policy.start();
 
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%m%n");
        encoder.start();
 
        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setContext(context);
        encoder1.setPattern("%d %p (%file:%line\\)- %m%n");
        encoder1.start();
 
        appender.setRollingPolicy(policy);
        appender.setEncoder(encoder);
        //appender.setTriggeringPolicy(sizeBasedTriggeringPolicy);
        appender.start();
 
        /*设置动态日志控制台输出*/
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setContext(context);
        consoleAppender.setEncoder(encoder1);
        consoleAppender.start();
        logger.addAppender(consoleAppender);
 
        logger.addAppender(appender);
 
        return logger;
    }

    public static void main(String[] args) {
        Logger logger = new LoggerBuilder2().getLogger("1");
        String msg = "";
        for(int i = 0; i<10000; i++)
        {
            msg += "sdfsdfsdfsdfdsfdsfdsfdsfs";
        }

        for(int i = 0; i<1000; i++)
        {
            logger.info("{},{}",i, i*10);
        }


    }
}

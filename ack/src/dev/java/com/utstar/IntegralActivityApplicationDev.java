package com.utstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 *
 * @author UTSC0928
 * @date 2018/5/30
 */
@SpringBootApplication(scanBasePackages = "com.utstar")
public class IntegralActivityApplicationDev {

    private static void init() {
        System.out.println(System.getProperty("user.dir"));

        System.setProperty("spring.config.location","D:\\svn_idea\\ack\\src\\main\\config\\application.properties");
        System.setProperty("spring.pid.fail-on-write-error","true");
        System.setProperty("spring.pid.file","D:\\svn_idea\\ack\\ack.pid");
        System.setProperty("logging.config","D:\\svn_idea\\ack\\src\\main\\config\\logback.xml");
        System.setProperty("integral.home","D:\\svn_idea\\ack\\src\\main");
    }

    public static void main(String[] args) {
        init();

        SpringApplication springApplication = new SpringApplication(IntegralActivityApplicationDev.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);

    }
}

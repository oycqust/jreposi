package com.utstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * @author UTSC0928
 * @date 2018/5/30
 */
@SpringBootApplication(scanBasePackages = "com.utstar")
@EnableScheduling
public class IntegralActivityApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(IntegralActivityApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }
}

package com.bylink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BylinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(BylinkApplication.class, args);
    }
}

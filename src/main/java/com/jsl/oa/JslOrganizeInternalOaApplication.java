package com.jsl.oa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JslOrganizeInternalOaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JslOrganizeInternalOaApplication.class, args);
    }

}

package com.jsl.oa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类
 *
 * @since v1.0.0-SNAPSHOT
 * @version 1.0.0-SNAPSHOT
 * @author xiao_lfeng
 */
@SpringBootApplication
@EnableScheduling
public class JslOrganizeInternalOaApplication {

    /**
     * 入口
     *
     * @param args 参数
     */
    public static void main(String[] args) {
        SpringApplication.run(JslOrganizeInternalOaApplication.class, args);
    }

}

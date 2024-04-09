package com.jsl.oa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * <h1>邮件配置类</h1>
 * <hr/>
 * 用于配置邮件发送相关信息
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author 筱锋xiao_lfeng
 */
@Configuration
public class MailConfiguration {

    @Value("${spring.mail.host}")
    private String emailHost;
    @Value("${spring.mail.username}")
    private String emailUsername;
    @Value("${spring.mail.password}")
    private String emailPassword;

    /**
     * 配置并返回一个用于发送邮件的JavaMailSender实例。
     * <p>
     * 此方法使用SMTP协议设置邮件发送器，启用SMTP认证和STARTTLS。同时，为了便于故障排除，
     * 开启了邮件调试功能，以记录邮件发送过程。配置细节如主机、端口、用户名和密码根据应用程序的属性进行设置。
     * </p>
     *
     * @return 配置好的JavaMailSender实例，可用于发送邮件。
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setHost(emailHost);
        mailSender.setPort(25);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


}

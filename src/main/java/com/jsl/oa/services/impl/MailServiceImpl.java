package com.jsl.oa.services.impl;

import com.jsl.oa.annotations.UserAbleToUse;
import com.jsl.oa.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * <h1>邮件服务实现类</h1>
 * <hr/>
 * 用于发送邮件
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author 筱锋xiao_lfeng
 * @see MailService
 * @see JavaMailSender
 * @see MimeMessageHelper
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    @UserAbleToUse
    public void sendMail(String sendTo, String subject, String text) {
        //发送多媒体邮件
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(sendTo);
            helper.setSubject(subject);
            helper.setText(text, true);

            javaMailSender.send(message);
            log.info("\t> 发送邮件 {} 标题 {} 成功", sendTo, subject);
        } catch (MessagingException e) {
            //TODO: 10001-发送邮件失败处理
            log.error("\t> 邮件发送失败", e);
        }

    }

    @Override
    @Async
    @UserAbleToUse
    public void sendMail(String sendTo, String model) { }

    @Override
    @Async
    @UserAbleToUse
    public void sendMailAboutUserLogin(String email, Integer code) {
        // 发送邮件带HTML模块部分
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessage = new MimeMessageHelper(message, true);
            mimeMessage.setFrom(from);
            mimeMessage.setTo(email);
            mimeMessage.setSubject("用户登陆邮件");
            Context context = new Context();
            context.setVariable("code", code);
            context.setVariable("email", email);
            String emailContent = templateEngine.process("./mail/user-login.html", context);
            mimeMessage.setText(emailContent, true);

            javaMailSender.send(message);
            log.info("\t> 发送登陆邮件给 {} 登陆验证码 {}", email, code);
        } catch (MessagingException e) {
            //TODO: 10001-发送邮件失败处理
            log.error("\t> 邮件发送失败", e);
        }
    }
}

package com.jsl.oa.services.impl;

import com.jsl.oa.services.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Override
    public void sendMail() {
        //发送多媒体邮件
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            //第二个参数控制着附件上传
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            String to = "lfengzeng@vip.qq.com";
            helper.setTo(to);
            String subject = "springboot测试邮件";
            helper.setSubject(subject);

            //第二个参数表示以 html 语法解析文本
            String text = "你好：这是一封测试邮件。请坚持下去。加油!";
            helper.setText(text, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            //TODO: 10001-发送邮件失败处理
        }

    }
}

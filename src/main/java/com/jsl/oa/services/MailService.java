package com.jsl.oa.services;

import javax.mail.MessagingException;

public interface MailService {
    void sendMail() throws MessagingException;
}


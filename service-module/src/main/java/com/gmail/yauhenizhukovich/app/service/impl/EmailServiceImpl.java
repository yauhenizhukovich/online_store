package com.gmail.yauhenizhukovich.app.service.impl;

import java.lang.invoke.MethodHandles;

import com.gmail.yauhenizhukovich.app.service.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static com.gmail.yauhenizhukovich.app.service.constant.MailConstant.FROM_EMAIL;

@Component
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    public final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {this.emailSender = emailSender;}

    @Override
    public boolean sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(FROM_EMAIL);
        try {
            emailSender.send(message);
            logger.debug("Email was successfully sent.");
            return true;
        } catch (MailException e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

}

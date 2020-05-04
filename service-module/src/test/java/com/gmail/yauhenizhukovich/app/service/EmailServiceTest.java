package com.gmail.yauhenizhukovich.app.service;

import com.gmail.yauhenizhukovich.app.service.impl.EmailServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static com.gmail.yauhenizhukovich.app.service.constant.MailConstant.FROM_EMAIL;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.SUBJECT;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.TEXT;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.TO;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        emailService = new EmailServiceImpl(mailSender);
    }

    @Test
    public void sendMessage_returnTrue() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(TO);
        message.setSubject(SUBJECT);
        message.setText(TEXT);
        boolean isSent = emailService.sendMessage(TO, SUBJECT, TEXT);
        verify(mailSender, times(1)).send(message);
        Assertions.assertThat(isSent).isNotNull();
        Assertions.assertThat(isSent).isTrue();
    }

}

package com.gmail.yauhenizhukovich.app.service.util;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailUtil {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SEND_FROM_SMTP = "smtp.gmail.com";
    private static final String SEND_FROM_EMAIL = "enter_your_email@gmail.com";
    private static final String SEND_FROM_PASSWORD = "enter_your_password";
    private static final String MESSAGE_TITLE = "Check out to get access to our online store!";
    private static final String MESSAGE_TEXT = "This is your password. Use it on the login page to access our resources: ";

    public static void sendPassword(String email, String password) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", SEND_FROM_SMTP);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(properties, null);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(SEND_FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email, false));
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse("", false));
            message.setSubject(MESSAGE_TITLE);
            message.setText(MESSAGE_TEXT + password);
            message.setSentDate(new Date());
            SMTPTransport transport = (SMTPTransport) session.getTransport("smtp");
            transport.connect(SEND_FROM_SMTP, SEND_FROM_EMAIL, SEND_FROM_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ex) {
            logger.error(ex.getMessage());
        }
    }

}

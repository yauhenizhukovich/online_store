package com.gmail.yauhenizhukovich.app.service;

public interface EmailService {

    boolean sendMessage(String to, String subject, String text);

}

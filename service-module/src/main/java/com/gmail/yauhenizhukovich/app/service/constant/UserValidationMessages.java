package com.gmail.yauhenizhukovich.app.service.constant;

public interface UserValidationMessages {

    String NOT_EMPTY_NAME_MESSAGE = "Name cannot be empty.";
    String NAME_SIZE_MESSAGE = "All fields should be filled with length from 2 to 20 for first name or 40 to others.";
    String NAME_PATTERN_MESSAGE = "Name can contain only letters.";
    String NOT_EMPTY_EMAIL_MESSAGE = "Email cannot be empty.";
    String EMAIL_PATTERN_MESSAGE = "Email does not match email-pattern.";

}

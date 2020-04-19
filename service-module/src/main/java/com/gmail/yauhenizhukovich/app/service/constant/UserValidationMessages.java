package com.gmail.yauhenizhukovich.app.service.constant;

import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_FIRSTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_LASTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_PATRONYMIC_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MIN_NAME_SIZE;

public interface UserValidationMessages {

    String NOT_EMPTY_FIRSTNAME_MESSAGE = "First name cannot be empty.";
    String NOT_EMPTY_LASTNAME_MESSAGE = "Last name cannot be empty.";
    String NOT_NULL_PATRONYMIC_MESSAGE = "Patronymic cannot be null.";
    String FIRSTNAME_SIZE_MESSAGE = "First name length should be from " + MIN_NAME_SIZE + " to " + MAX_FIRSTNAME_SIZE + ".";
    String LASTNAME_SIZE_MESSAGE = "Last name length should be from " + MIN_NAME_SIZE + " to " + MAX_LASTNAME_SIZE + ".";
    String PATRONYMIC_SIZE_MESSAGE = "Patronymic length should be less than " + MAX_PATRONYMIC_SIZE + ".";
    String FIRSTNAME_PATTERN_MESSAGE = "First name can contain only letters.";
    String LASTNAME_PATTERN_MESSAGE = "Last name can contain only letters.";
    String PATRONYMIC_PATTERN_MESSAGE = "Patronymic can contain only letters.";
    String NOT_EMPTY_EMAIL_MESSAGE = "Email cannot be empty.";
    String EMAIL_PATTERN_MESSAGE = "Email does not match email pattern.";
    String NOT_NULL_ADDRESS_MESSAGE = "Address cannot be null.";
    String ADDRESS_PATTERN_MESSAGE = "Address can contain letters, digits, spaces, commas and dots.";
    String NOT_NULL_TELEPHONE_MESSAGE = "Telephone cannot be null.";
    String TELEPHONE_PATTERN_MESSAGE = "Telephone can contain only digits, dashes and one plus symbol.";

}

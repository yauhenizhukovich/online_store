package com.gmail.yauhenizhukovich.app.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.yauhenizhukovich.app.service.model.UserDTO;

import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.EMAIL_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.EMAIL_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_FIRSTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_LASTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_PATRONYMIC_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MIN_NAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.NAME_PATTERN;

public class ValidationUtil {

    public static void validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.find()) {
            throw new IllegalArgumentException(EMAIL_PATTERN_MESSAGE);
        }
    }

    public static void validateUserDTO(UserDTO userDTO) {
        String firstName = userDTO.getFirstName();
        validateFirstName(firstName);
        String lastName = userDTO.getLastName();
        validateLastName(lastName);
        String patronymic = userDTO.getPatronymic();
        validatePatronymic(patronymic);
        String email = userDTO.getEmail();
        validateEmail(email);
    }

    private static void validateFirstName(String firstName) {
        int length = firstName.length();
        if (length < MIN_NAME_SIZE || length > MAX_FIRSTNAME_SIZE) {
            throw new IllegalArgumentException(NAME_SIZE_MESSAGE);
        }
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(firstName);
        if (!matcher.find()) {
            throw new IllegalArgumentException(NAME_PATTERN_MESSAGE);
        }
    }

    private static void validateLastName(String lastName) {
        int length = lastName.length();
        if (length < MIN_NAME_SIZE || length > MAX_LASTNAME_SIZE) {
            throw new IllegalArgumentException(NAME_SIZE_MESSAGE);
        }
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(lastName);
        if (!matcher.find()) {
            throw new IllegalArgumentException(NAME_PATTERN_MESSAGE);
        }
    }

    private static void validatePatronymic(String patronymic) {
        int length = patronymic.length();
        if (length < MIN_NAME_SIZE || length > MAX_PATRONYMIC_SIZE) {
            throw new IllegalArgumentException(NAME_SIZE_MESSAGE);
        }
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(patronymic);
        if (!matcher.find()) {
            throw new IllegalArgumentException(NAME_PATTERN_MESSAGE);
        }
    }

}

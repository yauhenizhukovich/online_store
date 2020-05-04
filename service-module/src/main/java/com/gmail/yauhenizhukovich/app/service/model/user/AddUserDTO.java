package com.gmail.yauhenizhukovich.app.service.model.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.EMAIL_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.FIRSTNAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.FIRSTNAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.LASTNAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.LASTNAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_EMAIL_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_FIRSTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_LASTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_PATRONYMIC_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.PATRONYMIC_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.PATRONYMIC_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.EMAIL_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_FIRSTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_LASTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_PATRONYMIC_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MIN_NAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.NAME_PATTERN;

public class AddUserDTO {

    @NotEmpty(message = NOT_EMPTY_FIRSTNAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_FIRSTNAME_SIZE, message = FIRSTNAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = FIRSTNAME_PATTERN_MESSAGE)
    private String firstName;
    @NotEmpty(message = NOT_EMPTY_LASTNAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_LASTNAME_SIZE, message = LASTNAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = LASTNAME_PATTERN_MESSAGE)
    private String lastName;
    @NotEmpty(message = NOT_EMPTY_PATRONYMIC_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_PATRONYMIC_SIZE, message = PATRONYMIC_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = PATRONYMIC_PATTERN_MESSAGE)
    private String patronymic;
    @NotEmpty(message = NOT_EMPTY_EMAIL_MESSAGE)
    @Pattern(regexp = EMAIL_PATTERN, message = EMAIL_PATTERN_MESSAGE)
    private String email;
    @NotNull
    private RoleEnumService role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public RoleEnumService getRole() {
        return role;
    }

    public void setRole(RoleEnumService role) {
        this.role = role;
    }

}

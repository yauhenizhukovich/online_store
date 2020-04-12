package com.gmail.yauhenizhukovich.app.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.EMAIL_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_EMAIL_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_NAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.EMAIL_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_FIRSTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_LASTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_PATRONYMIC_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MIN_NAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.NAME_PATTERN;

public class UserDTO {

    private String uniqueNumber;
    @NotEmpty(message = NOT_EMPTY_NAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_FIRSTNAME_SIZE, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String firstName;
    @NotEmpty(message = NOT_EMPTY_NAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_LASTNAME_SIZE, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String lastName;
    @NotEmpty(message = NOT_EMPTY_NAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_PATRONYMIC_SIZE, message = NAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = NAME_PATTERN_MESSAGE)
    private String patronymic;
    @NotEmpty(message = NOT_EMPTY_EMAIL_MESSAGE)
    @Pattern(regexp = EMAIL_PATTERN, message = EMAIL_PATTERN_MESSAGE)
    private String email;
    private String password;
    private RoleEnum role;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", uniqueNumber='" + uniqueNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(uniqueNumber, userDTO.uniqueNumber) &&
                Objects.equals(firstName, userDTO.firstName) &&
                Objects.equals(lastName, userDTO.lastName) &&
                Objects.equals(patronymic, userDTO.patronymic) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(password, userDTO.password) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueNumber, firstName, lastName, patronymic, email, password, role);
    }

}

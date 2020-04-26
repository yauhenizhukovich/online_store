package com.gmail.yauhenizhukovich.app.service.model;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.ADDRESS_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.EMAIL_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.FIRSTNAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.FIRSTNAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.LASTNAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.LASTNAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_EMAIL_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_FIRSTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_EMPTY_LASTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_NULL_ADDRESS_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_NULL_PATRONYMIC_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.NOT_NULL_TELEPHONE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.PATRONYMIC_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.PATRONYMIC_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationMessages.TELEPHONE_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.ADDRESS_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.EMAIL_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_FIRSTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_LASTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MAX_PATRONYMIC_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.MIN_NAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.NAME_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.UserValidationRules.TELEPHONE_PATTERN;

public class UserDTO {

    private String uniqueNumber;
    @NotEmpty(message = NOT_EMPTY_FIRSTNAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_FIRSTNAME_SIZE, message = FIRSTNAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = FIRSTNAME_PATTERN_MESSAGE)
    private String firstName;
    @NotEmpty(message = NOT_EMPTY_LASTNAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_LASTNAME_SIZE, message = LASTNAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = LASTNAME_PATTERN_MESSAGE)
    private String lastName;
    @NotNull(message = NOT_NULL_PATRONYMIC_MESSAGE)
    @Size(max = MAX_PATRONYMIC_SIZE, message = PATRONYMIC_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = PATRONYMIC_PATTERN_MESSAGE)
    private String patronymic;
    @NotEmpty(message = NOT_EMPTY_EMAIL_MESSAGE)
    @Pattern(regexp = EMAIL_PATTERN, message = EMAIL_PATTERN_MESSAGE)
    private String email;
    private String password;
    @NotNull(message = NOT_NULL_ADDRESS_MESSAGE)
    @Pattern(regexp = ADDRESS_PATTERN, message = ADDRESS_PATTERN_MESSAGE)
    private String address;
    @NotNull(message = NOT_NULL_TELEPHONE_MESSAGE)
    @Pattern(regexp = TELEPHONE_PATTERN, message = TELEPHONE_PATTERN_MESSAGE)
    private String telephone;
    @NotNull
    private RoleEnumService role;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnumService getRole() {
        return role;
    }

    public void setRole(RoleEnumService role) {
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
                "uniqueNumber='" + uniqueNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", role=" + role +
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
                Objects.equals(address, userDTO.address) &&
                Objects.equals(telephone, userDTO.telephone) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueNumber, firstName, lastName, patronymic, email, password, address, telephone, role);
    }

}

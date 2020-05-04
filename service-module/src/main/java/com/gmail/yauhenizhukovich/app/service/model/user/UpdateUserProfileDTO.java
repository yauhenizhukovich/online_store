package com.gmail.yauhenizhukovich.app.service.model.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.ADDRESS_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.FIRSTNAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.FIRSTNAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.LASTNAME_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.LASTNAME_SIZE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.NOT_EMPTY_FIRSTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.NOT_EMPTY_LASTNAME_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.NOT_NULL_ADDRESS_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.NOT_NULL_TELEPHONE_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationMessages.TELEPHONE_PATTERN_MESSAGE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationRules.ADDRESS_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationRules.MAX_FIRSTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationRules.MAX_LASTNAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationRules.MIN_NAME_SIZE;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationRules.NAME_PATTERN;
import static com.gmail.yauhenizhukovich.app.service.constant.validation.UserValidationRules.TELEPHONE_PATTERN;

public class UpdateUserProfileDTO {

    @NotEmpty(message = NOT_EMPTY_FIRSTNAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_FIRSTNAME_SIZE, message = FIRSTNAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = FIRSTNAME_PATTERN_MESSAGE)
    private String firstName;
    @NotEmpty(message = NOT_EMPTY_LASTNAME_MESSAGE)
    @Size(min = MIN_NAME_SIZE, max = MAX_LASTNAME_SIZE, message = LASTNAME_SIZE_MESSAGE)
    @Pattern(regexp = NAME_PATTERN, message = LASTNAME_PATTERN_MESSAGE)
    private String lastName;
    @NotNull(message = NOT_NULL_ADDRESS_MESSAGE)
    @Pattern(regexp = ADDRESS_PATTERN, message = ADDRESS_PATTERN_MESSAGE)
    private String address;
    @NotNull(message = NOT_NULL_TELEPHONE_MESSAGE)
    @Pattern(regexp = TELEPHONE_PATTERN, message = TELEPHONE_PATTERN_MESSAGE)
    private String telephone;
    private boolean updatePassword;

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

    public boolean getUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(boolean updatePassword) {
        this.updatePassword = updatePassword;
    }

}

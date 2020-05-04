package com.gmail.yauhenizhukovich.app.service.constant.validation;

public interface UserValidationRules {

    int MIN_NAME_SIZE = 2;
    int MAX_FIRSTNAME_SIZE = 20;
    int MAX_LASTNAME_SIZE = 40;
    int MAX_PATRONYMIC_SIZE = 40;
    String NAME_PATTERN = "^[a-zA-Z]*$";
    String EMAIL_PATTERN = "^[a-zA-Z0-9_]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    String ADDRESS_PATTERN = "^[a-zA-Z0-9 ,.]*$";
    String TELEPHONE_PATTERN = "^[0-9+-]*$";

}
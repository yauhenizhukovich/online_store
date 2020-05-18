package com.gmail.yauhenizhukovich.app.service.exception;

public class AdministratorChangingException extends Throwable {

    public static final String ADMINISTRATOR_CHANGING_EXCEPTION_MESSAGE = "You should have at least one administrator.";

    public AdministratorChangingException() {
        super(ADMINISTRATOR_CHANGING_EXCEPTION_MESSAGE);
    }

}

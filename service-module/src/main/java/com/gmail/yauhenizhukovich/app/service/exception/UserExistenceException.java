package com.gmail.yauhenizhukovich.app.service.exception;

public class UserExistenceException extends Throwable {

    public static final String USER_EXISTENCE_EXCEPTION_MESSAGE = "User with this email already exists.";

    public UserExistenceException() { super(USER_EXISTENCE_EXCEPTION_MESSAGE); }

}

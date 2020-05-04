package com.gmail.yauhenizhukovich.app.service.exception;

public class AnonymousUserException extends Throwable {

    public static final String ANONYMOUS_USER_EXCEPTION_MESSAGE = "You do not have access to these features";

    public AnonymousUserException() {
        super(ANONYMOUS_USER_EXCEPTION_MESSAGE);
    }

}

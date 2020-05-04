package com.gmail.yauhenizhukovich.app.service.exception;

public class CustomerDetailsException extends Throwable {

    public static final String CUSTOMER_DETAILS_EXCEPTION_MESSAGE = "You need to fill in address and phone number in your profile to place an order.";

    public CustomerDetailsException() {
        super(CUSTOMER_DETAILS_EXCEPTION_MESSAGE);
    }

}

package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class AppExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ResponseError handleValidationError(MethodArgumentNotValidException e) {
        ResponseError responseError = new ResponseError();
        List<String> errors = new ArrayList<>();
        List<ObjectError> bindingResultErrors = e.getBindingResult().getAllErrors();
        bindingResultErrors.forEach(error -> errors.add(error.getDefaultMessage()));
        responseError.setErrors(errors);
        return responseError;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleError404()   {
        return "404";
    }

}

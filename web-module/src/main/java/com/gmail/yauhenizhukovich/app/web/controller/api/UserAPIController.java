package com.gmail.yauhenizhukovich.app.web.controller.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserAPIController {

    private final UserService userService;

    public UserAPIController(UserService userService) {this.userService = userService;}

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Object addUser(
            @RequestBody @Valid AddUserDTO user,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            List<ObjectError> bindingResultErrors = bindingResult.getAllErrors();
            bindingResultErrors.forEach(error -> errors.add(error.getDefaultMessage()));
            return errors;
        }
        try {
            return userService.addUser(user);
        } catch (UserExistenceException e) {
            return e.getMessage();
        }
    }

}

package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.List;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.user.AddUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UsersDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {

    public static final String UPDATE_USER_MESSAGE = "User successfully updated.";
    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping
    public String getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        List<UsersDTO> users = userService.getUsersByPage(page);
        model.addAttribute("users", users);
        int countOfPages = userService.getCountOfPages();
        model.addAttribute("pages", countOfPages);
        return "users";
    }

    @GetMapping("/add")
    public String getAddUserPage(
            Model model
    ) {
        AddUserDTO user = new AddUserDTO();
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleEnumService.values());
        return "add_user";
    }

    @PostMapping
    public String addUser(
            @ModelAttribute(name = "user") @Valid AddUserDTO user,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return getAddUserPageWithErrors(user, model);
        }
        try {
            UserDTO addedUser = userService.addUser(user);
            return getUserPage(model, addedUser);
        } catch (UserExistenceException e) {
            model.addAttribute("error", e.getMessage());
            return getAddUserPageWithErrors(user, model);
        }
    }

    @GetMapping("/{uniqueNumber}")
    public String getUser(
            @PathVariable String uniqueNumber,
            Model model
    ) {
        UserDTO user = userService.getUserByUniqueNumber(uniqueNumber);
        return getUserPage(model, user);
    }

    @PostMapping("/delete")
    public String deleteUsers(
            @RequestParam(defaultValue = "") List<String> uniqueNumbers
    ) {
        if (!uniqueNumbers.isEmpty()) {
            try {
                userService.deleteUsersByUniqueNumber(uniqueNumbers);
            } catch (AdministratorChangingException e) {
                return "redirect:/users?message=" + e.getMessage();
            }
        }
        return "redirect:/users";
    }

    @PostMapping("/{uniqueNumber}")
    public String updateUser(
            @PathVariable String uniqueNumber,
            @ModelAttribute(name = "user") @Valid UpdateUserDTO updatedUser,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return getUserPage(model, updatedUser);
        }
        updatedUser.setUniqueNumber(uniqueNumber);
        UserDTO user;
        try {
            user = userService.updateUser(updatedUser);
            model.addAttribute("message", UPDATE_USER_MESSAGE);
        } catch (AdministratorChangingException e) {
            user = userService.getUserByUniqueNumber(uniqueNumber);
            model.addAttribute("error", e.getMessage());
        }
        return getUserPage(model, user);
    }

    private String getUserPage(Model model, UserDTO addedUser) {
        model.addAttribute("user", addedUser);
        model.addAttribute("roles", RoleEnumService.values());
        return "user";
    }

    private String getUserPage(Model model, UpdateUserDTO addedUser) {
        model.addAttribute("user", addedUser);
        model.addAttribute("roles", RoleEnumService.values());
        return "user";
    }

    private String getAddUserPageWithErrors(
            @ModelAttribute(name = "user") @Valid AddUserDTO user,
            Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleEnumService.values());
        return "add_user";
    }

}


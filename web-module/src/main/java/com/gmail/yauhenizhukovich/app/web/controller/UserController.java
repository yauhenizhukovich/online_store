package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnum;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/users")
public class UserController {

    static final String UPDATE_PASSWORD_MESSAGE = "Password successfully updated.";
    private UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping
    public String getAllUsers() {
        return "redirect:/users/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String getUsersByPage(@PathVariable Integer pageNumber, Model model) {
        List<UserDTO> users = userService.getUsersByPage(pageNumber);
        model.addAttribute("users", users);
        List<Integer> pages = userService.getListOfPageNumbers();
        model.addAttribute("pages", pages);
        model.addAttribute("pageNumber", pageNumber);
        RoleEnum[] roles = RoleEnum.values();
        model.addAttribute("roles", roles);
        return "users";
    }

    @GetMapping("/add")
    public String addUserPage(Model model) {
        model.addAttribute("user", new UserDTO());
        RoleEnum[] roles = RoleEnum.values();
        model.addAttribute("roles", roles);
        return "add_user";
    }

    @PostMapping("/add")
    public String addUser(
            @ModelAttribute(name = "user") @Valid UserDTO user,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            RoleEnum[] roles = RoleEnum.values();
            model.addAttribute("roles", roles);
            return "add_user";
        } else {
            userService.addUser(user);
            return "redirect:/users";
        }
    }

    @PostMapping("/{uniqueNumber}/update/role")
    public String updateUserRole(
            @PathVariable String uniqueNumber,
            @RequestParam RoleEnum userRole,
            @RequestParam Integer pageNumber
    ) {
        try {
            userService.updateRoleByUniqueNumber(userRole, uniqueNumber);
        } catch (AdministratorChangingException e) {
            return "redirect:/users/page/" + pageNumber + "?error=" + e.getMessage();
        }
        return "redirect:/users/page/" + pageNumber;
    }

    @GetMapping("/{uniqueNumber}/update/password")
    public String updateUserPassword(
            @PathVariable String uniqueNumber,
            @RequestParam String pageNumber
    ) {
        userService.updatePasswordByUniqueNumber(uniqueNumber);
        return "redirect:/users/page/" + pageNumber + "?message=" + UPDATE_PASSWORD_MESSAGE;
    }

    @PostMapping("/delete")
    public String deleteUsers(
            @RequestParam String[] uniqueNumbers,
            @RequestParam String pageNumber
    ) {
        List<String> uniqueNumbersList = Arrays.asList(uniqueNumbers);
        try {
            userService.deleteUsersByUniqueNumber(uniqueNumbersList);
        } catch (AdministratorChangingException e) {
            return "redirect:/users/page/" + pageNumber + "?error=" + e.getMessage();
        }
        return "redirect:/users";
    }

}

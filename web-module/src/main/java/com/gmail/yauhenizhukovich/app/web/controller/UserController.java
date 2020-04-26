package com.gmail.yauhenizhukovich.app.web.controller;

import java.util.Arrays;
import java.util.List;
import javax.naming.Binding;
import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AdministratorChangingException;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.exception.UserExistenceException;
import com.gmail.yauhenizhukovich.app.service.model.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {

    static final String UPDATE_PASSWORD_MESSAGE = "Password successfully updated.";
    static final String UPDATE_ROLE_MESSAGE = "Role successfully updated.";
    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping
    public String getUsers(@RequestParam(required = false) Integer page, Model model) {
        if (page == null) {
            page = 1;
        }
        List<UserDTO> users = userService.getUsersByPage(page);
        model.addAttribute("users", users);
        List<Integer> pages = userService.getPages();
        model.addAttribute("pages", pages);
        return "users";
    }

    @GetMapping("/add")
    public String getAddUserPage(Model model) {
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        RoleEnumService[] roles = RoleEnumService.values();
        model.addAttribute("roles", roles);
        return "add_user";
    }

    @PostMapping
    public String addUser(
            @ModelAttribute(name = "user") @Valid UserDTO user,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", RoleEnumService.values());
            return "add_user";
        } else {
            try {
                user = userService.addUser(user);
            } catch (UserExistenceException e) {
                model.addAttribute("error", e.getMessage());
                return "add_user";
            }
            model.addAttribute("user", user);
            model.addAttribute("roles", RoleEnumService.values());
            return "user";
        }
    }

    @GetMapping("/{uniqueNumber}")
    public String getUserPage(
            @PathVariable String uniqueNumber,
            Model model
    ) {
        UserDTO user = userService.getUserByUniqueNumber(uniqueNumber);
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleEnumService.values());
        return "user";
    }

    @PostMapping("/{uniqueNumber}")
    public String updateUser(
            @PathVariable String uniqueNumber,
            @RequestParam(required = false) RoleEnumService role,
            @RequestParam(required = false) String destination,
            Model model
    ) {
        UserDTO user = new UserDTO();
        if (destination != null) {
            user = userService.updatePasswordByUniqueNumber(uniqueNumber);
            model.addAttribute("passwordMessage", UPDATE_PASSWORD_MESSAGE);
            if (destination.equals("profile")) {
                addAttributesForProfilePage(user, model, user);
                return "profile";
            }
        }
        if (role != null) {
            try {
                user = userService.updateRoleByUniqueNumber(role, uniqueNumber);
                model.addAttribute("roleMessage", UPDATE_ROLE_MESSAGE);
            } catch (AdministratorChangingException e) {
                user = userService.getUserByUniqueNumber(uniqueNumber);
                addAttributesWhenException(model, user, e);
                return "user";
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleEnumService.values());
        return "user";
    }

    private void addAttributesWhenException(Model model, UserDTO user, AdministratorChangingException e) {
        model.addAttribute("roleMessage", e.getMessage());
        model.addAttribute("user", user);
        model.addAttribute("roles", RoleEnumService.values());
    }

    @PostMapping("/delete")
    public String deleteUsers(
            @RequestParam(required = false) String[] uniqueNumbers
    ) {
        if (uniqueNumbers != null) {
            List<String> uniqueNumbersList = Arrays.asList(uniqueNumbers);
            try {
                userService.deleteUsersByUniqueNumber(uniqueNumbersList);
            } catch (AdministratorChangingException e) {
                return "redirect:/users?message=" + e.getMessage();
            }
        }
        return "redirect:/users";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        try {
            UserDTO user = userService.getUserProfile();
            model.addAttribute("user", user);
            model.addAttribute("updatedUser", user);
        } catch (AnonymousUserException e) {
            return "redirect:/?message=" + e.getMessage();
        }
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @ModelAttribute(name = "updatedUser") @Valid UserDTO updatedUser,
            BindingResult bindingResult,
            Model model
    ) throws AnonymousUserException {
        UserDTO user = userService.getUserProfile();
        if (bindingResult.hasErrors()) {
            addAttributesForProfilePage(updatedUser, model, user);
            return "profile";
        }
        user = userService.updateUserDetails(updatedUser);
        addAttributesForProfilePage(updatedUser, model, user);
        return "profile";
    }

    private void addAttributesForProfilePage(
            @ModelAttribute(name = "updatedUser") @Valid UserDTO updatedUser,
            Model model,
            UserDTO user
    ) {
        model.addAttribute("user", user);
        model.addAttribute("updatedUser", updatedUser);
        model.addAttribute("roles", RoleEnumService.values());
    }

}


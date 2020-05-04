package com.gmail.yauhenizhukovich.app.web.controller;

import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService;
import com.gmail.yauhenizhukovich.app.service.model.user.UpdateUserProfileDTO;
import com.gmail.yauhenizhukovich.app.service.model.user.UserProfileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    public static final String PASSWORD_UPDATE_MESSAGE = "Password successfully updated.";
    private final UserService userService;

    public ProfileController(UserService userService) {this.userService = userService;}

    @GetMapping
    public String getUserProfile(
            Model model
    ) {
        try {
            UserProfileDTO user = userService.getUserProfile();
            return getProfile(user, model);
        } catch (AnonymousUserException e) {
            return "redirect:/?message=" + e.getMessage();
        }
    }

    @PostMapping
    public String updateProfile(
            @ModelAttribute(name = "updatedUser") @Valid UpdateUserProfileDTO updatedUser,
            BindingResult bindingResult,
            Model model
    ) throws AnonymousUserException {
        if (bindingResult.hasErrors()) {
            return getProfile(updatedUser, model);
        }
        UserProfileDTO user = userService.updateUserProfile(updatedUser);
        if (updatedUser.getUpdatePassword()) {
            model.addAttribute("updatePassword", PASSWORD_UPDATE_MESSAGE);
        }
        return getProfile(user, model);

    }

    private String getProfile(
            @ModelAttribute(name = "updatedUser") @Valid UpdateUserProfileDTO updatedUser,
            Model model
    ) throws AnonymousUserException {
        UserProfileDTO user = userService.getUserProfile();
        model.addAttribute("user", user);
        model.addAttribute("updatedUser", updatedUser);
        model.addAttribute("roles", RoleEnumService.values());
        return "profile";
    }

    private String getProfile(
            UserProfileDTO user,
            Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("updatedUser", user);
        model.addAttribute("roles", RoleEnumService.values());
        return "profile";
    }

}

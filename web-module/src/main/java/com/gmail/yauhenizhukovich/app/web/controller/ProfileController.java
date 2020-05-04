package com.gmail.yauhenizhukovich.app.web.controller;

import javax.validation.Valid;

import com.gmail.yauhenizhukovich.app.service.OrderService;
import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.exception.AnonymousUserException;
import com.gmail.yauhenizhukovich.app.service.model.ObjectsDTOAndPagesEntity;
import com.gmail.yauhenizhukovich.app.service.model.order.OrdersDTO;
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
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.yauhenizhukovich.app.web.constant.MessageConstant.PASSWORD_UPDATE_MESSAGE;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final OrderService orderService;

    public ProfileController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

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
            model.addAttribute("update", true);
            return getProfile(updatedUser, model);
        }
        UserProfileDTO user = userService.updateUserProfile(updatedUser);
        if (updatedUser.getUpdatePassword()) {
            model.addAttribute("message", PASSWORD_UPDATE_MESSAGE);
        }
        return getProfile(user, model);

    }

    @GetMapping("/orders")
    public String getUserOrdersByProfile(
            @RequestParam(defaultValue = "1") Integer page,
            Model model
    ) {
        try {
            ObjectsDTOAndPagesEntity<OrdersDTO> orders = orderService.getUserOrdersByPage(page);
            model.addAttribute("orders", orders.getObjects());
            model.addAttribute("pages", orders.getPages());
            return "orders";
        } catch (AnonymousUserException e) {
            return "redirect:/?message=" + e.getMessage();
        }
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

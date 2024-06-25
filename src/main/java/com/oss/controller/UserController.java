package com.oss.controller;

import com.oss.dto.UserRegistrationDto;
import com.oss.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.oss.model.User;
@Controller
public class UserController {
    @Autowired
    private userservice userService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "manager/userlist";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "common/register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") User user, Model model) {
        try {
            userService.registerNewUser(user);
            model.addAttribute("successMessage", "Registration successful!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was an error during registration. Please try again.");
        }
        return "common/register";
    }
}
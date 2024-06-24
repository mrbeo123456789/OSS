package com.oss.controller;

import com.oss.dto.UserRegistrationDto;
import com.oss.model.User;
import com.oss.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private userservice userService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "manager/userlist";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            model.addAttribute("user", user);
            switch (user.getRole().getRoleName()) {
                case "MANAGER":
                    return "redirect:/manager/dashboard";
                case "SALESTAFF":
                    return "redirect:/salestaff/dashboard";
                case "INVENTORYSTAFF":
                    return "redirect:/inventorystaff/dashboard";
                case "CUSTOMER":
                    return "redirect:/customer/dashboard";
                default:
                    return "redirect:/dashboard";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "common/register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userDto, Model model) {
        try {
            userService.registerNewUser(userDto);
            model.addAttribute("successMessage", "Registration successful! A confirmation email has been sent.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was an error during registration. Please try again.");
        }
        return "common/register";
    }
}
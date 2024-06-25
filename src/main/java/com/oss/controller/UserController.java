package com.oss.controller;

import com.oss.dto.UserRegistrationDto;
import com.oss.model.Role;
import com.oss.model.User;
import com.oss.service.roleservice;
import com.oss.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private userservice userService;
    @Autowired
    private roleservice roleService;
    @GetMapping("/user/list")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "manager/userlist";
    }

    @GetMapping("/user/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("roles", roleService.getListRole());
        return "manager/addnewuser";
    }

    @PostMapping("/user/add")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("fullname") String fullName,
                          @RequestParam("email") String email,
                          @RequestParam("number") String mobile,
                          @RequestParam("department") Long roleId,
                          RedirectAttributes redirectAttributes) {
        Role role = roleService.getRoleById(roleId);
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setRole(role);

        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("userAdded", true);

        return "redirect:/user/list";
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
package com.oss.controller;

import com.oss.model.Role;
import com.oss.model.User;
import com.oss.service.roleservice;
import com.oss.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;


@Controller
public class UserController {
    @Autowired
    private userservice userService;
    @Autowired
    private roleservice roleService;

    @GetMapping("/")
    public String home() {
        return "common/home";
    }

    @GetMapping("/user/list")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "manager/userlist";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "common/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.login(email, password);
        if (user != null) {
            model.addAttribute("user", user);
            switch (user.getRole().getRoleName()) {
                case "MANAGER":
                    return "redirect:/templates/manager/dashboard.html";
                case "SALESTAFF":
                    return "redirect:/home";
                case "INVENTORYSTAFF":
                    return "redirect:/templates/inventory/dashboard.html";
                case "CUSTOMER":
                    return "redirect:/home";
                default:
                    return "redirect:/home";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/home";
        }
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
                          @RequestParam("imageData") String avatarFile,
                          RedirectAttributes redirectAttributes) {
        Role role = roleService.getRoleById(roleId);
        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setRole(role);
        user.setAvatar(avatarFile);
        try {
            userService.saveUser(user);
        } catch (Exception e){
            e.printStackTrace();
        }
try{
    userService.saveUser(user);
}catch (Exception E)
{
    E.printStackTrace();
}


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

    @GetMapping("/home")
    public String showHomePage(Model model) {
        return "common/home";
    }
}
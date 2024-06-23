package com.oss.controller;

import com.oss.model.User;
import com.oss.service.userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private userservice userService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        System.out.println(users);
        model.addAttribute("users", users);
        return "manager/userlist";
    }

}

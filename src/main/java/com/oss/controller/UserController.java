package com.oss.controller;

import com.oss.model.Role;
import com.oss.model.User;
import com.oss.service.roleservice;
import com.oss.service.userservice;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserController {
    @Autowired
    private userservice userService;
    @Autowired
    private roleservice roleService;
    @Autowired
    private HttpSession httpSession;



    @GetMapping("/user/list")
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "5") int size,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(required = false) String role) {
        if ("All".equals(role)) {
            role = null; // Handle the "All" value by setting role to null
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.findUsers(keyword, role, pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElements", userPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);

        return "manager/userlist";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "common/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.login(email, password);
        httpSession.setAttribute("user", user);
        if (user != null) {
            model.addAttribute("user", user);
            Long userRoleId = user.getRole().getRoleId();
            switch (Integer.parseInt(userRoleId.toString())) {
                case 1:
                    return "redirect:/home";
                case 2:
                    return "redirect:/";
                case 3:
                    return "redirect:/products";
                case 4:
                    return "redirect:/user/list";
                default:
                    return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/";
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
                          RedirectAttributes redirectAttributes,
                          Model model) {
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid role ID");
            return "redirect:/user/list";
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setRole(role);
        user.setAvatar(avatarFile);

        try {
            boolean check = userService.saveUser(user);
            System.out.println(check);
            if (!check) {

                model.addAttribute("error", "Username already exists");
                model.addAttribute("roles", roleService.getListRole()); // Assuming you have a method to fetch all roles
                return "manager/addnewuser";
            } else {
                redirectAttributes.addFlashAttribute("userAdded", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add user");
        }

        return "redirect:/user/list";
    }

    @GetMapping("/user/delete")
    public String deleteUser(@RequestParam("userid") String userid){
      userService.deleteUserById(Long.parseLong(userid));
        return "redirect:/user/list";
    }
    @GetMapping("/user/update")
    public String updateUser(@RequestParam("userid") String userid,Model  model){
        model.addAttribute("roles", roleService.getListRole());
        model.addAttribute("user",userService.getUserByUserid(userid));
        return "manager/edituser";
    }
    @PostMapping("/user/edit")
    public String updateUser(@ModelAttribute("user") User user,Model model, @RequestParam("imageData") String avatarFile) {

        try {
          user.setAvatar(avatarFile);
            System.out.println(user.getAvatar());
           userService.updateUser(user);
                model.addAttribute("roles", roleService.getListRole()); // Assuming you have a method to fetch all roles

        } catch (Exception e) {
            e.printStackTrace();

        }

        return "redirect:/user/list";
    }
    @GetMapping("/verify")
    public String verifyUser(@RequestParam("email") String email, @RequestParam("code") String verificationCode, Model model) {
        boolean isVerified = userService.verifyUser(email, verificationCode);

        if (isVerified) {
            model.addAttribute("message", "Verification successful! Your account is now active.");
        } else {
            model.addAttribute("message", "Invalid verification code or the code has expired.");
        }

        return "common/thankyou";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "common/register";
    }



    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") User user, Model model) {
        try {

            user.setRole(roleService.getRoleById(2L));
            userService.registerNewUser(user);
            model.addAttribute("successMessage", "A mail has to sent to your mail!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "There was an error during registration. Please try again.");
        }
        return "common/register";
    }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        return "common/home";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        if (httpSession.getAttribute("user") != null) {
            return "common/profile";
        }else{
            return "redirect:/login";
        }

    }
}
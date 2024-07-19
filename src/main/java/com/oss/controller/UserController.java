package com.oss.controller;

import com.oss.model.ProductImage;
import com.oss.model.Role;
import com.oss.model.User;
import com.oss.service.EmailService;
import com.oss.service.roleservice;
import com.oss.service.userservice;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;


@Controller
public class UserController {
    @Autowired
    private userservice userService;
    @Autowired
    private roleservice roleService;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;


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
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.login(username, password);
        if (user != null) {
            httpSession.setAttribute("user", user);
            model.addAttribute("user", user);
            Long userRoleId = user.getRole().getRoleId();
            switch (userRoleId.intValue()) {
                case 1:
                    return "redirect:/manager";
                case 2:
                    return "redirect:/customer";
                case 3:
                    return "redirect:/salestaff";
                case 4:
                    return "redirect:/warehousestaff";

                default:
                    return "redirect:/";
            }
        } else {

            model.addAttribute("error", "Invalid username or password");
            return "redirect:/login";

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
    @GetMapping("/manager")
    public String ManagerDashBoard(Model model) {
        return "manager/dashboard";
    }
    @GetMapping("/warehousestaff")
    public String WHSDashBoard(Model model) {
        return "inventory/dashboard";
    }
    @GetMapping("/salestaff")
    public String SSDashBoard(Model model) {
        return "sale/dashboard";
    }
    @GetMapping("/customer")
    public String CustomerDashBoard(Model model) {
        return "customer/shop";
    }
    @GetMapping("/home")
    public String showHomePage() {
        return "redirect:/";
    }


    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        User reqUser = (User) httpSession.getAttribute("user");
        //check if user is logged in
        if ( reqUser!= null) {
            //load user profile
            User user = userService.getUserByUserid(reqUser.getId().toString());
            model.addAttribute("user", user);
            return "common/profile";
        }else{
            return "redirect:/login";
        }

    }

    @PostMapping("/changeavatar")
    public String changeAvatar(Model model,
                                @RequestParam("newavatar") MultipartFile avatar){
        User reqUser = (User) httpSession.getAttribute("user");
        //save new avatar image
        // Upload file
        if (avatar.isEmpty()) {
            model.addAttribute("message", "Image is required");
        } else {
            String uploadFolder = "Avt/"; // adjust this path
            try {
                // Generate a unique filename for the image
                String filename = UUID.randomUUID().toString() + "_" + avatar.getOriginalFilename();
                Path path = Paths.get(uploadFolder + filename);

                // Ensure the directory exists
                Files.createDirectories(path.getParent());

                // Delete current image
                if(reqUser.getAvatar()!=null){
                    Files.delete(Path.of(reqUser.getAvatar()));
                }

                // Save image file to the system
                byte[] bytes = avatar.getBytes();
                Files.write(path, bytes);

                // Change avatar path for user
                User user = userService.getUserByUserid(reqUser.getId().toString());
                user.setAvatar("./Avt/" + filename);
                userService.updateUser(user);


            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to upload file");
                return "redirect:/profile";
            }
        }
        return "redirect:/profile";

    }

    @PostMapping("/changepassword")
    public String changeUserPassword(Model model,
                                     @RequestParam("currentPassword") String currentPassword,
                                     @RequestParam("newPassword") String newPassword) throws IOException {
        User reqUser = (User) httpSession.getAttribute("user");

        // Check current password is correct
        if (userService.login(reqUser.getUsername(), currentPassword) == null) {
            model.addAttribute("wrongpwmessage", "Password is incorrect");
        } else {
            // Change password
            User user = userService.getUserByUserid(reqUser.getId().toString());
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(user);
        }

        return "redirect:/profile";
    }


    @GetMapping("/logout")
    public String logOut(){
        httpSession.removeAttribute("user");
        return "redirect:/";
    }

    @PostMapping("changeemail")
    public String sendOtp(@RequestParam("newemail") String newEmail,Model model){
        //generate verify code
        String verifyCode = generateOTP(6);
        //set timer for verify code
        httpSession.setMaxInactiveInterval(360);
        //save verify code to session
        httpSession.setAttribute("verifyCode", verifyCode);

        String message = "<p>Here your verification code</p></br>"+
                "<p>Your verification code will expire in 5 minutes</p>";

        User user = (User) httpSession.getAttribute("user");

        //send verification code to old
        emailService.sendVerifycationCode(user.getEmail(),message+verifyCode);
        model.addAttribute("newemail", newEmail);
        return "common/otpcheck";
    }

    public static String generateOTP(int length) {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(digits.charAt(random.nextInt(digits.length())));
        }

        return otp.toString();
    }

    @PostMapping("verifychangeemail")
    public String verifyChangeEmail(Model model,
                                    @RequestParam("newemail") String newEmail,
                                    @RequestParam("verifycode") String verifyCode) throws IOException {
        String code = httpSession.getAttribute("verifyCode").toString();
        if(!verifyCode.equals(code)){
            model.addAttribute("wrongcodemsg", "Wrong verify code, please enter again");
            model.addAttribute("newemail", newEmail);
            return "common/otpcheck";
        }else{
            User reqUser = (User) httpSession.getAttribute("user");
            User user = userService.getUserByUserid(reqUser.getId().toString());
            user.setEmail(newEmail);
            userService.updateUser(user);
            httpSession.removeAttribute("user");
            return "redirect:/";
        }
    }

    @PostMapping("changephonenumber")
    public String changePhoneNumber(Model model, @RequestParam("newnumber") String newNumber) throws IOException {
        User reqUser = (User) httpSession.getAttribute("user");
        User user = userService.getUserByUserid(reqUser.getId().toString());
        user.setMobile(newNumber);
        userService.updateUser(user);
        return "redirect:/profile";
    }
}
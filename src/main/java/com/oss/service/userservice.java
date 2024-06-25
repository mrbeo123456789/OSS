package com.oss.service;

import com.oss.dto.UserRegistrationDto;
import com.oss.model.User;
import com.oss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class userservice {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public User registerNewUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email đã được đăng ký");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Log thông tin người dùng sau khi lưu
        System.out.println("User registered successfully: " + savedUser);

        return savedUser;
    }
    public List<User> getAllUsers() {
        return userRepository.findAllWithRoles();
    }

    public void saveUser(User user) {
        String randomPassword = generateRandomPassword();

        // Encode the password
        String hashedPassword = passwordEncoder.encode(randomPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }
    private String generateRandomPassword() {
        int length = 8;
        String charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charset.length());
            sb.append(charset.charAt(index));
        }
        return sb.toString();
    }
}
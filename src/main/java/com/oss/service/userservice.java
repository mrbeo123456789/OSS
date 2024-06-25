package com.oss.service;

import com.oss.dto.UserRegistrationDto;
import com.oss.model.User;
import com.oss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return userRepository.findAll();
    }
}
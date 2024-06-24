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


    public User registerNewUser(UserRegistrationDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("Email đã được đăng ký");
        }

        String hashedPassword = passwordEncoder.encode(userDto.getPassword());

        User newUser = new User(
                userDto.getFullName(),
                userDto.getUsername(),
                userDto.getEmail(),
                userDto.getMobile(),
                hashedPassword
        );

        return userRepository.save(newUser);
    }
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
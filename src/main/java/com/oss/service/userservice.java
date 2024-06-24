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
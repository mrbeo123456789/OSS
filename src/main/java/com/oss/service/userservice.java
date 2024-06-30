package com.oss.service;

import com.oss.model.User;
import com.oss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class userservice {
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Value("${image.upload.dir}")
    private String imageUploadDir;

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

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    public List<User> getAllUsers() {

        return userRepository.findAllWithRoles();
    }

    public void saveUser(User user) throws IOException {
        // Generate a random password
        String randomPassword = generateRandomPassword();

        // Encode the random password before setting it to user object
        String hashedPassword = passwordEncoder.encode(randomPassword);
        user.setPassword(hashedPassword);

        // Create the directory if it doesn't exist
        File directory = new File(imageUploadDir);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + imageUploadDir);
            }
        }

        // Check if avatar data is present
        if (user.getAvatar() != null && user.getAvatar().startsWith("data:image")) {
            // Decode the Base64 image string to bytes
            String[] avatarParts = user.getAvatar().split(",");
            String base64Data = avatarParts.length > 1 ? avatarParts[1] : avatarParts[0]; // Handle edge case

            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Define the file path for the new image (username.jpg)
            String imagePath = imageUploadDir + "/" + user.getUsername() + ".jpg";
            System.out.println("Image Path: " + imagePath);

            // Save the image bytes to the file
            try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                fos.write(imageBytes);
                System.out.println("Image saved successfully.");
            } catch (IOException e) {
                throw new IOException("Failed to save image: " + e.getMessage());
            }


            // Set the relative avatar image path in the user object
            String relativeImagePath = "/assets/images/Avt/" + user.getUsername() + ".jpg";
            user.setAvatar(relativeImagePath);
        }

        // Save the user object (including the hashed password and avatar path)
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
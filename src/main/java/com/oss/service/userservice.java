package com.oss.service;

import com.oss.model.User;
import com.oss.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
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
import java.time.LocalDateTime;
import java.util.*;

@Service
public class userservice {
    private String uploadDir;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void registerNewUser(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email đã được đăng ký");
        }
        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        user.setVerified(false);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
         emailService.sendVerificationEmail(user.getEmail(), verificationCode);



    }
    public User getUserByUserid(String userid) {
        return userRepository.getReferenceById(Long.parseLong(userid));
    }
    public void deleteUserById(Long userId) {

        userRepository.deleteById(userId);
    }
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    public Page<User> findUsers(String keyword, String role, Pageable pageable) {
        if (keyword != null && role != null) {
            return userRepository.findByFullNameContainingAndRole_RoleName(keyword, role, pageable);
        } else if (keyword != null) {
            return userRepository.findByFullNameContaining(keyword, pageable);
        } else if (role != null) {
            return userRepository.findByRole_RoleName(role, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    public boolean saveUser(User user) throws IOException {
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            return false; // Username already exists
        }

        // Save register date
        user.setRegisteredDate(new Date());

        // Generate a random password
        String randomPassword = generateRandomPassword();
        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);
        user.setVerified(false);
        emailService.sendVerificationEmail(user.getEmail(), verificationCode,randomPassword);
        // Encode the random password before setting it to the user object
        String hashedPassword = passwordEncoder.encode(randomPassword);
        user.setPassword(hashedPassword);

        // Save the avatar image if present
        saveAvatarImage(user);

        // Save the user object (including the hashed password and avatar path)
        userRepository.save(user);
        return true;
    }
    public void updateUser(User updatedUser) throws IOException {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Update fields of existingUser with updatedUser's data
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setMobile(updatedUser.getMobile());
        existingUser.setRole(updatedUser.getRole());
        saveAvatarImage(updatedUser);
        existingUser.setAvatar(updatedUser.getAvatar());

        // Save the updated user
        userRepository.save(existingUser);
    }
    private final String imageUploadDir = "./Avt";

    private void saveAvatarImage(User user) throws IOException {
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

            // Delete existing image if it exists
            File existingImageFile = new File(imagePath);
            if (existingImageFile.exists()) {
                if (!existingImageFile.delete()) {
                    throw new IOException("Failed to delete existing image: " + imagePath);
                }
            }

            // Save the new image bytes to the file
            try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                fos.write(imageBytes);
            } catch (IOException e) {
                throw new IOException("Failed to save image: " + e.getMessage());
            }

            // Set the relative avatar image path in the user object
            String relativeImagePath = "/Avt/" + user.getUsername() + ".jpg";
            user.setAvatar(relativeImagePath);
        }
    }
    public boolean verifyUser(String email, String verificationCode) {
        Optional<User> userOptional = userRepository.findByEmailAndVerificationCode(email, verificationCode);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.isVerified()) {
                user.setVerified(true);
                user.setVerificationCode(null); // Clear the verification code once verified
                userRepository.save(user);
                return true;
            }
        }

        return false;
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
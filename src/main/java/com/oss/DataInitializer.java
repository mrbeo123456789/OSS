package com.oss;

import com.oss.model.Role;
import com.oss.model.User;
import com.oss.repository.RoleRepository;
import com.oss.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
//            Role adminRole = new Role();
//            adminRole.setRoleName("ADMIN");
//            Role userRole = new Role();
//            userRole.setRoleName("ROLE_USER");
//
//         //  roleRepository.saveAll(List.of(adminRole, userRole));
//
//            // Create some users
//            User user1 = new User();
//            user1.setFullName("John Doe");
//            user1.setUsername("johndoe");
//            user1.setEmail("johndoe@example.com");
//            user1.setMobile("1234567890");
//            user1.setPassword("password1"); // In a real application, ensure to encode the password
//            user1.setRegisteredDate(new Date());
//            user1.setLastLogin(new Date());
//            user1.setAvatar("avatar1.png");
//            user1.setRole(adminRole);
//            User user2 = new User();
//            user2.setFullName("Jane Smith");
//            user2.setUsername("janesmith");
//            user2.setEmail("janesmith@example.com");
//            user2.setMobile("0987654321");
//            user2.setPassword("password2"); // In a real application, ensure to encode the password
//            user2.setRegisteredDate(new Date());
//            user2.setLastLogin(new Date());
//            user2.setAvatar("avatar2.png");
//            user1.setRole(userRole);
//            userRepository.saveAll(List.of(user1, user2));
        };
    }
}

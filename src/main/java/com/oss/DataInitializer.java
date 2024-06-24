package com.oss;

import com.oss.model.Role;
import com.oss.model.User;
import com.oss.repository.ProductRepository;
import com.oss.repository.RoleRepository;
import com.oss.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, ProductRepository productRepository) {
        return args -> {
         //
        };
    }
}

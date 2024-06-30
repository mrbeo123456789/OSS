package com.oss.config;

import com.oss.model.Category;
import com.oss.model.Role;
import com.oss.model.User;
import com.oss.repository.CategoryRepository;
import com.oss.repository.RoleRepository;
import com.oss.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Initialize roles
            Role adminRole = new Role();
            adminRole.setRoleName("admin");

            Role customerRole = new Role();
            customerRole.setRoleName("customer");

            Role warehouseStaffRole = new Role();
            warehouseStaffRole.setRoleName("warehousestaff");

            Role salesStaffRole = new Role();
            salesStaffRole.setRoleName("salesstaff");

            // Save roles to the database
            roleRepository.save(adminRole);
            roleRepository.save(customerRole);
            roleRepository.save(warehouseStaffRole);
            roleRepository.save(salesStaffRole);

            // Initialize categories
            Category shoesCategory = new Category();
            shoesCategory.setCategoryName("Shoes");

            Category racketCategory = new Category();
            racketCategory.setCategoryName("Racket");

            Category clothesCategory = new Category();
            clothesCategory.setCategoryName("Clothes");

            // Save categories to the database
            categoryRepository.save(shoesCategory);
            categoryRepository.save(racketCategory);
            categoryRepository.save(clothesCategory);

            // Initialize and save users with roles
            User adminUser = new User();
            adminUser.setFullName("Admin User");
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setMobile("123456789");
            adminUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            adminUser.setRegisteredDate(new Date());
            adminUser.setLastLogin(new Date());
            adminUser.setAvatar("admin-avatar.jpg");
            adminUser.setRole(adminRole);

            userRepository.save(adminUser);

            User customerUser = new User();
            customerUser.setFullName("Customer User");
            customerUser.setUsername("customer");
            customerUser.setEmail("customer@example.com");
            customerUser.setMobile("987654321");
            customerUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            customerUser.setRegisteredDate(new Date());
            customerUser.setLastLogin(new Date());
            customerUser.setAvatar("customer-avatar.jpg");
            customerUser.setRole(customerRole);

            userRepository.save(customerUser);

            User warehouseStaffUser = new User();
            warehouseStaffUser.setFullName("Warehouse Staff User");
            warehouseStaffUser.setUsername("warehousestaff");
            warehouseStaffUser.setEmail("warehousestaff@example.com");
            warehouseStaffUser.setMobile("555555555");
            warehouseStaffUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            warehouseStaffUser.setRegisteredDate(new Date());
            warehouseStaffUser.setLastLogin(new Date());
            warehouseStaffUser.setAvatar("warehousestaff-avatar.jpg");
            warehouseStaffUser.setRole(warehouseStaffRole);

            userRepository.save(warehouseStaffUser);

            User salesStaffUser = new User();
            salesStaffUser.setFullName("Sales Staff User");
            salesStaffUser.setUsername("salesstaff");
            salesStaffUser.setEmail("salesstaff@example.com");
            salesStaffUser.setMobile("444444444");
            salesStaffUser.setPassword("$2a$10$Fz7pNp5VYswz4nrVsgsXfOw0J2yXsIW7VxNJ4wC68HFSUTbI6Bfdi");
            salesStaffUser.setRegisteredDate(new Date());
            salesStaffUser.setLastLogin(new Date());
            salesStaffUser.setAvatar("salesstaff-avatar.jpg");
            salesStaffUser.setRole(salesStaffRole);

            userRepository.save(salesStaffUser);
        };
    }
}


package com.example.EWaste_Management_System;

import com.example.EWaste_Management_System.Entity.Role;
import com.example.EWaste_Management_System.Entity.User;
import com.example.EWaste_Management_System.Repository.RoleRepository;
import com.example.EWaste_Management_System.Repository.UserRepository;
import java.util.HashSet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppStartupRunner {
       @Bean
    CommandLineRunner seed(RoleRepository roleRepo,
                           UserRepository userRepo,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepo.findByName("ADMIN") == null) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepo.save(adminRole);
            }
            if (roleRepo.findByName("USER") == null) {
                Role userRole = new Role();
                userRole.setName("USER");
                roleRepo.save(userRole);
            }

            // create dev admin if not exists
            if (userRepo.findByEmail("admin@local") == null) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@local");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEnabled(true);
                admin.setRoles(new HashSet<>());
                admin.getRoles().add(roleRepo.findByName("ADMIN"));
                userRepo.save(admin);
                System.out.println("Created default admin -> admin@local / admin123");
            }
        };
    }
}

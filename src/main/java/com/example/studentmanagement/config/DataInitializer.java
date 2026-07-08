package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.RoleRepository;
import com.example.studentmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Create Roles if they don't exist
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() ->
                        roleRepository.save(new Role(null, "ROLE_ADMIN"))
                );

        roleRepository.findByName("ROLE_TEACHER")
                .orElseGet(() ->
                        roleRepository.save(new Role(null, "ROLE_TEACHER"))
                );

        roleRepository.findByName("ROLE_STUDENT")
                .orElseGet(() ->
                        roleRepository.save(new Role(null, "ROLE_STUDENT"))
                );

        // Create default admin user if it doesn't exist
        userRepository.findByUsername("admin")
                .orElseGet(() -> {

                    User admin = new User();

                    admin.setUsername("admin");
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setRole(adminRole);
                    admin.setDeleted(false);

                    // No Student linked to admin
                    admin.setStudent(null);

                    return userRepository.save(admin);
                });
    }
}
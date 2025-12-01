package com.example.center_management.config;

import com.example.center_management.domain.entity.User;
import com.example.center_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminUsername = "admin";

        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }

        User admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode("admin123"))
                .fullName("Administrator")
                .role("ADMIN")
                .build();

        userRepository.save(admin);

        System.out.println("🔥 Default ADMIN created: username=admin, password=admin123");
    }
}

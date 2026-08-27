package com.photography.backend.config;

import com.photography.backend.entity.AdminUser;
import com.photography.backend.repository.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminDataInitializer.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:}")
    private String adminEmail;

    @Value("${admin.password:}")
    private String adminPassword;

    public AdminDataInitializer(AdminUserRepository adminUserRepository, PasswordEncoder passwordEncoder) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (adminEmail == null || adminEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
            logger.info("No valid ADMIN_EMAIL or ADMIN_PASSWORD specified in environment. Skipping automatic admin user creation.");
            return;
        }

        String cleanEmail = adminEmail.trim().toLowerCase();
        if (adminUserRepository.existsByEmail(cleanEmail)) {
            logger.info("Admin user with email '{}' already exists. Skipping initialization.", cleanEmail);
            return;
        }

        String hashedPassword = passwordEncoder.encode(adminPassword);
        AdminUser admin = new AdminUser(cleanEmail, hashedPassword, "ADMIN");
        adminUserRepository.save(admin);

        logger.info("Initial admin user successfully created with email: {}", cleanEmail);
    }
}

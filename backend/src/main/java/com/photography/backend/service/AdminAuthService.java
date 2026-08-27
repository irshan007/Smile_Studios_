package com.photography.backend.service;

import com.photography.backend.dto.AdminUserInfoDTO;
import com.photography.backend.dto.LoginRequestDTO;
import com.photography.backend.dto.LoginResponseDTO;
import com.photography.backend.entity.AdminUser;
import com.photography.backend.exception.UnauthorizedException;
import com.photography.backend.repository.AdminUserRepository;
import com.photography.backend.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthService.class);

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminAuthService(
            AdminUserRepository adminUserRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String email = request.getEmail().trim().toLowerCase();
        Optional<AdminUser> adminOpt = adminUserRepository.findByEmail(email);

        if (adminOpt.isEmpty()) {
            logger.warn("Authentication failed: User with email '{}' not found.", email);
            throw new UnauthorizedException("Invalid email or password");
        }

        AdminUser admin = adminOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            logger.warn("Authentication failed: Incorrect password for email '{}'.", email);
            throw new UnauthorizedException("Invalid email or password");
        }

        String role = admin.getRole() != null ? admin.getRole() : "ADMIN";
        String token = jwtTokenProvider.generateToken(admin.getEmail(), role);

        logger.info("Admin user successfully authenticated: {}", admin.getEmail());

        AdminUserInfoDTO userInfo = new AdminUserInfoDTO(admin.getEmail(), role);
        return new LoginResponseDTO(true, token, userInfo, "Login successful");
    }
}

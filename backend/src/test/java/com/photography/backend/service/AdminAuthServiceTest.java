package com.photography.backend.service;

import com.photography.backend.dto.LoginRequestDTO;
import com.photography.backend.dto.LoginResponseDTO;
import com.photography.backend.entity.AdminUser;
import com.photography.backend.exception.UnauthorizedException;
import com.photography.backend.repository.AdminUserRepository;
import com.photography.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminAuthServiceTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AdminAuthService adminAuthService;

    private AdminUser mockAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAdmin = new AdminUser("admin@smilestudios.com", "$2a$10$hashedPasswordHere", "ADMIN");
    }

    @Test
    @DisplayName("Should successfully authenticate and return JWT token when valid credentials supplied")
    void testLoginSuccess() {
        LoginRequestDTO request = new LoginRequestDTO("admin@smilestudios.com", "Password123!");

        when(adminUserRepository.findByEmail("admin@smilestudios.com")).thenReturn(Optional.of(mockAdmin));
        when(passwordEncoder.matches("Password123!", mockAdmin.getPasswordHash())).thenReturn(true);
        when(jwtTokenProvider.generateToken("admin@smilestudios.com", "ADMIN")).thenReturn("mock-jwt-token");

        LoginResponseDTO response = adminAuthService.login(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("admin@smilestudios.com", response.getUser().getEmail());
        assertEquals("ADMIN", response.getUser().getRole());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when password is incorrect")
    void testLoginIncorrectPassword() {
        LoginRequestDTO request = new LoginRequestDTO("admin@smilestudios.com", "WrongPassword");

        when(adminUserRepository.findByEmail("admin@smilestudios.com")).thenReturn(Optional.of(mockAdmin));
        when(passwordEncoder.matches("WrongPassword", mockAdmin.getPasswordHash())).thenReturn(false);

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> adminAuthService.login(request));
        assertEquals("Invalid email or password", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when user email does not exist")
    void testLoginUserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO("unknown@smilestudios.com", "Password123!");

        when(adminUserRepository.findByEmail("unknown@smilestudios.com")).thenReturn(Optional.empty());

        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> adminAuthService.login(request));
        assertEquals("Invalid email or password", ex.getMessage());
    }
}

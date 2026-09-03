package com.photography.backend.config;

import com.photography.backend.entity.AdminUser;
import com.photography.backend.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminDataInitializerTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminDataInitializer adminDataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create admin account when environment variables exist and admin does not exist")
    void testInitializerCreatesNewAdmin() {
        ReflectionTestUtils.setField(adminDataInitializer, "adminEmail", "admin@smilestudios.com");
        ReflectionTestUtils.setField(adminDataInitializer, "adminPassword", "SecurePassword123!");

        when(adminUserRepository.findByEmail("admin@smilestudios.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("SecurePassword123!")).thenReturn("$2a$10$encodedPassword");

        adminDataInitializer.run();

        verify(adminUserRepository, times(1)).save(any(AdminUser.class));
    }

    @Test
    @DisplayName("Should synchronize password hash when admin with email already exists")
    void testInitializerSynchronizesExistingAdmin() {
        ReflectionTestUtils.setField(adminDataInitializer, "adminEmail", "admin@smilestudios.com");
        ReflectionTestUtils.setField(adminDataInitializer, "adminPassword", "SecurePassword123!");

        AdminUser existingAdmin = new AdminUser("admin@smilestudios.com", "$2a$10$oldHash", "ADMIN");
        when(adminUserRepository.findByEmail("admin@smilestudios.com")).thenReturn(Optional.of(existingAdmin));
        when(passwordEncoder.encode("SecurePassword123!")).thenReturn("$2a$10$newHash");

        adminDataInitializer.run();

        verify(adminUserRepository, times(1)).save(existingAdmin);
    }

    @Test
    @DisplayName("Should skip creation when environment variables are missing")
    void testInitializerSkipsWhenVariablesMissing() {
        ReflectionTestUtils.setField(adminDataInitializer, "adminEmail", "");
        ReflectionTestUtils.setField(adminDataInitializer, "adminPassword", "");

        adminDataInitializer.run();

        verify(adminUserRepository, never()).findByEmail(anyString());
        verify(adminUserRepository, never()).save(any(AdminUser.class));
    }
}

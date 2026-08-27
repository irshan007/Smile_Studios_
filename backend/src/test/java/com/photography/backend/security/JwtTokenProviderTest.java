package com.photography.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", "SmileStudiosSecretJwtSigningKeyForBackendProductionSecurity2026!");
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", 3600000L); // 1 hour
    }

    @Test
    @DisplayName("Should generate, validate, and parse valid JWT token")
    void testTokenGenerationAndValidation() {
        String token = jwtTokenProvider.generateToken("admin@smilestudios.com", "ADMIN");

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("admin@smilestudios.com", jwtTokenProvider.getEmailFromToken(token));
        assertEquals("ADMIN", jwtTokenProvider.getRoleFromToken(token));
    }

    @Test
    @DisplayName("Should reject invalid or tampered JWT token")
    void testInvalidTokenRejection() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.invalid.payload";
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    @DisplayName("Should reject expired JWT token")
    void testExpiredTokenRejection() {
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpirationInMs", -1000L); // Expired 1 second ago
        String expiredToken = jwtTokenProvider.generateToken("admin@smilestudios.com", "ADMIN");

        assertFalse(jwtTokenProvider.validateToken(expiredToken));
    }
}

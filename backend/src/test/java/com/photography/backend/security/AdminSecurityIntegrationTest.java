package com.photography.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photography.backend.dto.LoginRequestDTO;
import com.photography.backend.entity.AdminUser;
import com.photography.backend.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminSecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        if (!adminUserRepository.existsByEmail("testadmin@smilestudios.com")) {
            AdminUser admin = new AdminUser("testadmin@smilestudios.com", passwordEncoder.encode("TestPassword123!"), "ADMIN");
            adminUserRepository.save(admin);
        }
    }

    @Test
    @DisplayName("Login with valid credentials returns JWT token")
    void testLoginSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("testadmin@smilestudios.com", "TestPassword123!");

        mockMvc.perform(post("/api/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value("testadmin@smilestudios.com"));
    }

    @Test
    @DisplayName("Login with invalid password returns 401 Unauthorized")
    void testLoginInvalidPassword() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("testadmin@smilestudios.com", "WrongPassword!");

        mockMvc.perform(post("/api/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Protected endpoint /api/admin/works rejects request without JWT token with 401")
    void testProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/admin/works"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Protected endpoint /api/admin/works rejects request with invalid JWT token with 401")
    void testProtectedEndpointWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/admin/works")
                        .header("Authorization", "Bearer invalid-token-string"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Protected endpoint /api/admin/works accepts request with valid JWT token")
    void testProtectedEndpointWithValidToken() throws Exception {
        String token = jwtTokenProvider.generateToken("testadmin@smilestudios.com", "ADMIN");

        mockMvc.perform(get("/api/admin/works")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Public endpoints remain accessible without JWT authentication")
    void testPublicEndpointsAccessible() throws Exception {
        mockMvc.perform(get("/api/works"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/gallery/categories"))
                .andExpect(status().isOk());
    }
}

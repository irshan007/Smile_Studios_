package com.photography.backend.controller;

import com.photography.backend.dto.PortfolioWorkResponseDTO;
import com.photography.backend.entity.Category;
import com.photography.backend.entity.PortfolioWork;
import com.photography.backend.exception.GlobalExceptionHandler;
import com.photography.backend.service.PortfolioWorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminPortfolioWorkControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PortfolioWorkService portfolioWorkService;

    @InjectMocks
    private AdminPortfolioWorkController adminPortfolioWorkController;

    private static final String API_KEY = "test_secret_key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(adminPortfolioWorkController, "adminApiKey", API_KEY);

        mockMvc = MockMvcBuilders.standaloneSetup(adminPortfolioWorkController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Admin endpoints should reject request with missing ADMIN_API_KEY header")
    void testMissingApiKeyRejection() throws Exception {
        mockMvc.perform(get("/api/admin/works"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or missing ADMIN_API_KEY header"));
    }

    @Test
    @DisplayName("Admin endpoints should reject request with invalid ADMIN_API_KEY header")
    void testInvalidApiKeyRejection() throws Exception {
        mockMvc.perform(get("/api/admin/works")
                        .header("ADMIN_API_KEY", "wrong_key"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Admin endpoints should accept request with valid ADMIN_API_KEY header")
    void testValidApiKeySuccess() throws Exception {
        Category category = new Category("Weddings", "weddings", 1);
        PortfolioWork work = new PortfolioWork("Arun Wedding", "arun-wedding", "Desc", category);
        work.setId(1L);

        when(portfolioWorkService.getAllWorksForAdmin()).thenReturn(List.of(new PortfolioWorkResponseDTO(work)));

        mockMvc.perform(get("/api/admin/works")
                        .header("ADMIN_API_KEY", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].title").value("Arun Wedding"));
    }
}

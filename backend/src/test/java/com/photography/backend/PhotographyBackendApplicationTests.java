package com.photography.backend;

import com.photography.backend.config.CloudinaryConfig;
import com.photography.backend.service.CloudinaryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PhotographyBackendApplicationTests {

    @Autowired
    private CloudinaryConfig cloudinaryConfig;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Test
    @DisplayName("Spring context should load CloudinaryConfig and CloudinaryService beans successfully")
    void contextLoads() {
        assertNotNull(cloudinaryConfig, "CloudinaryConfig bean should be loaded");
        assertNotNull(cloudinaryService, "CloudinaryService bean should be loaded");
        assertEquals("smile-studios/portfolio", cloudinaryService.getDefaultFolder());
    }
}

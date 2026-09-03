package com.photography.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@SpringBootApplication
public class PhotographyBackendApplication {

    private static final Logger logger = LoggerFactory.getLogger(PhotographyBackendApplication.class);

    public static void main(String[] args) {
        loadDotenv();
        SpringApplication.run(PhotographyBackendApplication.class, args);
    }

    /**
     * Loads local environment variables from backend/.env or .env into System properties.
     * Ensures environment variables (DB, Cloudinary, Admin credentials, JWT) are properly loaded
     * into Spring Environment during local development regardless of startup directory.
     */
    private static void loadDotenv() {
        File envFile = new File("backend/.env");
        if (!envFile.exists()) {
            envFile = new File(".env");
        }

        if (envFile.exists() && envFile.isFile()) {
            try {
                List<String> lines = Files.readAllLines(envFile.toPath());
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                        continue;
                    }
                    int eqIndex = trimmed.indexOf('=');
                    if (eqIndex > 0) {
                        String key = trimmed.substring(0, eqIndex).trim();
                        String value = trimmed.substring(eqIndex + 1).trim();
                        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }
                        if (System.getProperty(key) == null && System.getenv(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                }
                logger.info("Loaded local environment configuration from: {}", envFile.getAbsolutePath());
            } catch (Exception e) {
                logger.warn("Could not load local .env file: {}", e.getMessage());
            }
        }
    }
}

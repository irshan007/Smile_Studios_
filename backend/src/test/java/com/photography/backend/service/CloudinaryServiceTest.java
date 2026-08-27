package com.photography.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.photography.backend.dto.CloudinaryUploadResult;
import com.photography.backend.exception.CloudinaryStorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);

        // Inject configuration values into service
        ReflectionTestUtils.setField(cloudinaryService, "cloudName", "test_cloud");
        ReflectionTestUtils.setField(cloudinaryService, "apiKey", "test_key");
        ReflectionTestUtils.setField(cloudinaryService, "apiSecret", "test_secret");
        ReflectionTestUtils.setField(cloudinaryService, "defaultFolder", "smile-studios/portfolio");
    }

    @Test
    @DisplayName("Should return true for isConfigured when credentials are provided")
    void testIsConfiguredTrue() {
        assertTrue(cloudinaryService.isConfigured());
    }

    @Test
    @DisplayName("Should return false for isConfigured when cloudName is missing")
    void testIsConfiguredFalse() {
        ReflectionTestUtils.setField(cloudinaryService, "cloudName", "");
        assertFalse(cloudinaryService.isConfigured());
    }

    @Test
    @DisplayName("Should throw exception when uploading null file")
    void testUploadNullFile() {
        CloudinaryStorageException ex = assertThrows(CloudinaryStorageException.class, () ->
                cloudinaryService.uploadImage(null)
        );
        assertTrue(ex.getMessage().contains("cannot be null"));
    }

    @Test
    @DisplayName("Should throw exception when uploading empty file")
    void testUploadEmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[0]);
        CloudinaryStorageException ex = assertThrows(CloudinaryStorageException.class, () ->
                cloudinaryService.uploadImage(emptyFile)
        );
        assertTrue(ex.getMessage().contains("empty file"));
    }

    @Test
    @DisplayName("Should throw exception when uploading non-image file")
    void testUploadNonImageFile() {
        MockMultipartFile textFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        CloudinaryStorageException ex = assertThrows(CloudinaryStorageException.class, () ->
                cloudinaryService.uploadImage(textFile)
        );
        assertTrue(ex.getMessage().contains("must be an image"));
    }

    @Test
    @DisplayName("Should throw exception when file exceeds 10MB limit")
    void testUploadFileExceedingLimit() {
        byte[] largeData = new byte[10 * 1024 * 1024 + 1]; // 10MB + 1 byte
        MockMultipartFile largeFile = new MockMultipartFile("file", "large.jpg", "image/jpeg", largeData);
        CloudinaryStorageException ex = assertThrows(CloudinaryStorageException.class, () ->
                cloudinaryService.uploadImage(largeFile)
        );
        assertTrue(ex.getMessage().contains("exceeds maximum limit"));
    }

    @Test
    @DisplayName("Should successfully upload image and return CloudinaryUploadResult")
    void testUploadImageSuccess() throws IOException {
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", "sample.png", "image/png", "fake-image-bytes".getBytes()
        );

        Map<String, Object> uploadResponse = new HashMap<>();
        uploadResponse.put("public_id", "smile-studios/portfolio/sample_123");
        uploadResponse.put("secure_url", "https://res.cloudinary.com/demo/image/upload/v123/smile-studios/portfolio/sample_123.png");
        uploadResponse.put("url", "http://res.cloudinary.com/demo/image/upload/v123/smile-studios/portfolio/sample_123.png");
        uploadResponse.put("format", "png");
        uploadResponse.put("width", 800);
        uploadResponse.put("height", 600);
        uploadResponse.put("bytes", 15000);

        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResponse);

        CloudinaryUploadResult result = cloudinaryService.uploadImage(imageFile);

        assertNotNull(result);
        assertEquals("smile-studios/portfolio/sample_123", result.getPublicId());
        assertEquals("https://res.cloudinary.com/demo/image/upload/v123/smile-studios/portfolio/sample_123.png", result.getSecureUrl());
        assertEquals("png", result.getFormat());
        assertEquals(800, result.getWidth());
        assertEquals(600, result.getHeight());

        verify(uploader, times(1)).upload(any(byte[].class), any(Map.class));
    }

    @Test
    @DisplayName("Should successfully delete image given public ID")
    void testDeleteImageSuccess() throws IOException {
        Map<String, Object> deleteResponse = new HashMap<>();
        deleteResponse.put("result", "ok");

        when(uploader.destroy(eq("smile-studios/portfolio/sample_123"), any(Map.class))).thenReturn(deleteResponse);

        boolean deleted = cloudinaryService.deleteImage("smile-studios/portfolio/sample_123");
        assertTrue(deleted);

        verify(uploader, times(1)).destroy(eq("smile-studios/portfolio/sample_123"), any(Map.class));
    }

    @Test
    @DisplayName("Should throw exception when public ID is blank for deletion")
    void testDeleteImageBlankId() {
        CloudinaryStorageException ex = assertThrows(CloudinaryStorageException.class, () ->
                cloudinaryService.deleteImage("   ")
        );
        assertTrue(ex.getMessage().contains("cannot be null or blank"));
    }

    @Test
    @DisplayName("Live Cloudinary upload and delete test (runs if real credentials are fully configured)")
    void testLiveUploadAndDeleteIfConfigured() {
        String cloudName = System.getProperty("CLOUDINARY_CLOUD_NAME");
        if (cloudName == null || cloudName.isBlank()) {
            cloudName = System.getenv("CLOUDINARY_CLOUD_NAME");
        }

        String apiKey = System.getProperty("CLOUDINARY_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            apiKey = System.getenv("CLOUDINARY_API_KEY");
        }

        String apiSecret = System.getProperty("CLOUDINARY_API_SECRET");
        if (apiSecret == null || apiSecret.isBlank()) {
            apiSecret = System.getenv("CLOUDINARY_API_SECRET");
        }

        if (cloudName == null || cloudName.isBlank() ||
            apiKey == null || apiKey.isBlank() ||
            apiSecret == null || apiSecret.isBlank() ||
            "your_cloud_name".equals(cloudName) ||
            "your_api_key".equals(apiKey) ||
            "your_api_secret".equals(apiSecret)) {
            System.out.println("[INFO] Skipping live Cloudinary test because real credentials are not provided in environment.");
            return;
        }

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", "true");
        Cloudinary liveCloudinary = new Cloudinary(config);

        CloudinaryService liveService = new CloudinaryService(liveCloudinary);
        ReflectionTestUtils.setField(liveService, "cloudName", cloudName);
        ReflectionTestUtils.setField(liveService, "apiKey", apiKey);
        ReflectionTestUtils.setField(liveService, "apiSecret", apiSecret);
        ReflectionTestUtils.setField(liveService, "defaultFolder", "smile-studios/portfolio-test");

        // 1x1 Transparent PNG decoded from Base64
        byte[] tinyPngBytes = java.util.Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==");

        MockMultipartFile testFile = new MockMultipartFile(
                "file", "test_live_upload.png", "image/png", tinyPngBytes
        );

        System.out.println("[TEST] Performing live upload to Cloudinary...");
        CloudinaryUploadResult result = liveService.uploadImage(testFile);

        assertNotNull(result, "Upload result should not be null");
        assertNotNull(result.getPublicId(), "Public ID should not be null");
        assertNotNull(result.getSecureUrl(), "Secure URL should not be null");
        assertTrue(result.getSecureUrl().startsWith("https://"), "URL should be secure (HTTPS)");

        System.out.println("[TEST] Live Upload Successful!");
        System.out.println("       Public ID: " + result.getPublicId());
        System.out.println("       Secure URL: " + result.getSecureUrl());

        System.out.println("[TEST] Performing live deletion from Cloudinary...");
        boolean deleted = liveService.deleteImage(result.getPublicId());
        assertTrue(deleted, "Image should be successfully deleted from Cloudinary");
        System.out.println("[TEST] Live Deletion Successful!");
    }
}

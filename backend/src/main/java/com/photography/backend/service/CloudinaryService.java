package com.photography.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.photography.backend.dto.CloudinaryUploadResult;
import com.photography.backend.exception.CloudinaryStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB limit

    private final Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    @Value("${cloudinary.folder:smile-studios/portfolio}")
    private String defaultFolder;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Checks whether required Cloudinary credentials are set.
     */
    public boolean isConfigured() {
        return cloudName != null && !cloudName.isBlank()
                && apiKey != null && !apiKey.isBlank()
                && apiSecret != null && !apiSecret.isBlank();
    }

    /**
     * Returns configured default Cloudinary folder for portfolio images.
     */
    public String getDefaultFolder() {
        return defaultFolder;
    }

    /**
     * Upload an image file to Cloudinary using default configured folder.
     *
     * @param file MultipartFile to upload
     * @return CloudinaryUploadResult with secure URL and public ID
     */
    public CloudinaryUploadResult uploadImage(MultipartFile file) {
        return uploadImage(file, defaultFolder);
    }

    /**
     * Upload an image file to Cloudinary into a specified folder.
     *
     * @param file   MultipartFile to upload
     * @param folder Target Cloudinary folder
     * @return CloudinaryUploadResult with secure URL and public ID
     */
    public CloudinaryUploadResult uploadImage(MultipartFile file, String folder) {
        validateFile(file);

        if (!isConfigured()) {
            throw new CloudinaryStorageException(
                    "Cloudinary credentials are not configured. Please configure CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET."
            );
        }

        String targetFolder = (folder != null && !folder.isBlank()) ? folder : defaultFolder;

        try {
            logger.info("Uploading image '{}' ({} bytes, type: {}) to Cloudinary folder '{}'",
                    file.getOriginalFilename(), file.getSize(), file.getContentType(), targetFolder);

            Map<?, ?> uploadParams = ObjectUtils.asMap(
                    "folder", targetFolder,
                    "resource_type", "auto"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            if (uploadResult == null || !uploadResult.containsKey("public_id")) {
                throw new CloudinaryStorageException("Cloudinary upload failed: received invalid response from Cloudinary API.");
            }

            String publicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");
            String url = (String) uploadResult.get("url");
            String format = (String) uploadResult.get("format");
            Integer width = uploadResult.get("width") != null ? ((Number) uploadResult.get("width")).intValue() : null;
            Integer height = uploadResult.get("height") != null ? ((Number) uploadResult.get("height")).intValue() : null;
            Long bytes = uploadResult.get("bytes") != null ? ((Number) uploadResult.get("bytes")).longValue() : null;

            logger.info("Cloudinary upload successful. Public ID: {}, Secure URL: {}", publicId, secureUrl);

            return new CloudinaryUploadResult(publicId, secureUrl, url, format, width, height, bytes);

        } catch (IOException e) {
            logger.error("IO exception while reading file bytes for Cloudinary upload", e);
            throw new CloudinaryStorageException("Failed to read image file data: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Cloudinary API error during image upload", e);
            throw new CloudinaryStorageException("Cloudinary image upload failed: " + e.getMessage(), e);
        }
    }

    /**
     * Delete an image from Cloudinary using its public ID.
     *
     * @param publicId Cloudinary public ID of the image
     * @return true if successfully deleted
     */
    public boolean deleteImage(String publicId) {
        if (publicId == null || publicId.isBlank()) {
            throw new CloudinaryStorageException("Cloudinary public ID cannot be null or blank for deletion.");
        }

        if (!isConfigured()) {
            throw new CloudinaryStorageException(
                    "Cloudinary credentials are not configured. Please configure CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET."
            );
        }

        try {
            logger.info("Deleting image from Cloudinary with Public ID: {}", publicId);
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

            String resultStatus = (String) deleteResult.get("result");
            logger.info("Cloudinary deletion response for Public ID '{}': {}", publicId, resultStatus);

            if ("ok".equalsIgnoreCase(resultStatus) || "not found".equalsIgnoreCase(resultStatus)) {
                return true;
            } else {
                throw new CloudinaryStorageException("Failed to delete image from Cloudinary. Result: " + resultStatus);
            }
        } catch (Exception e) {
            logger.error("Cloudinary API error during deletion of Public ID: {}", publicId, e);
            throw new CloudinaryStorageException("Cloudinary image deletion failed: " + e.getMessage(), e);
        }
    }

    /**
     * Validates an incoming MultipartFile.
     */
    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new CloudinaryStorageException("Image file cannot be null.");
        }
        if (file.isEmpty()) {
            throw new CloudinaryStorageException("Cannot upload an empty file.");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CloudinaryStorageException("File size (" + file.getSize() + " bytes) exceeds maximum limit of 10MB.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.isBlank()) {
            if (!contentType.toLowerCase().startsWith("image/")) {
                throw new CloudinaryStorageException("Invalid file type '" + contentType + "'. File must be an image.");
            }
        } else {
            String filename = file.getOriginalFilename();
            if (filename == null || !isImageFilename(filename)) {
                throw new CloudinaryStorageException("File does not have a recognized image extension.");
            }
        }
    }

    private boolean isImageFilename(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".webp")
                || lower.endsWith(".gif") || lower.endsWith(".svg")
                || lower.endsWith(".bmp");
    }
}

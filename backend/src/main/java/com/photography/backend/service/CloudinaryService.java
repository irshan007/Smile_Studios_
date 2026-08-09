package com.photography.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final boolean isConfigured;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name:demo}") String cloudName,
            @Value("${cloudinary.api-key:123456789}") String apiKey,
            @Value("${cloudinary.api-secret:secret}") String apiSecret
    ) {
        if (cloudName != null && !cloudName.equals("demo") && !cloudName.isBlank()) {
            this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
            ));
            this.isConfigured = true;
        } else {
            this.cloudinary = null;
            this.isConfigured = false;
        }
    }

    public UploadResult uploadImage(MultipartFile file, String category) throws IOException {
        String publicId = category.toLowerCase().replaceAll("[^a-z0-9]", "_") + "_" + UUID.randomUUID().toString().substring(0, 8);

        if (isConfigured && cloudinary != null) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "smile_photography/" + category.toLowerCase().replaceAll("[^a-z0-9]", "_"),
                    "public_id", publicId,
                    "overwrite", true,
                    "resource_type", "image"
            ));

            String url = (String) uploadResult.get("secure_url");
            String resultPublicId = (String) uploadResult.get("public_id");
            return new UploadResult(url, resultPublicId);
        } else {
            String fallbackUrl = "https://images.unsplash.com/photo-1519741497674-611481863552?auto=format&fit=crop&q=80&w=1600";
            return new UploadResult(fallbackUrl, "demo_public_id_" + publicId);
        }
    }

    public void destroy(String publicId) throws IOException {
        if (isConfigured && cloudinary != null && publicId != null && !publicId.startsWith("demo_public_id_")) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    public static class UploadResult {
        private final String url;
        private final String publicId;

        public UploadResult(String url, String publicId) {
            this.url = url;
            this.publicId = publicId;
        }

        public String getUrl() {
            return url;
        }

        public String getPublicId() {
            return publicId;
        }
    }
}

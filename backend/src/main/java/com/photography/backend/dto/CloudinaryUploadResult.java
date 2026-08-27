package com.photography.backend.dto;

public class CloudinaryUploadResult {
    private String publicId;
    private String secureUrl;
    private String url;
    private String format;
    private Integer width;
    private Integer height;
    private Long bytes;

    public CloudinaryUploadResult() {
    }

    public CloudinaryUploadResult(String publicId, String secureUrl, String url, String format, Integer width, Integer height, Long bytes) {
        this.publicId = publicId;
        this.secureUrl = secureUrl;
        this.url = url;
        this.format = format;
        this.width = width;
        this.height = height;
        this.bytes = bytes;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }
}

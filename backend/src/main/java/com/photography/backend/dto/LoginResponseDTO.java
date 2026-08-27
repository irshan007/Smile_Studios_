package com.photography.backend.dto;

public class LoginResponseDTO {
    private boolean success;
    private String token;
    private AdminUserInfoDTO user;
    private String message;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean success, String token, AdminUserInfoDTO user, String message) {
        this.success = success;
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AdminUserInfoDTO getUser() {
        return user;
    }

    public void setUser(AdminUserInfoDTO user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

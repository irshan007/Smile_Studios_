package com.photography.backend.exception;

public class CloudinaryStorageException extends RuntimeException {
    public CloudinaryStorageException(String message) {
        super(message);
    }

    public CloudinaryStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

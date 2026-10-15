package com.emailsender.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailResponse {

    private boolean success;
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;

    public EmailResponse() {
    }

    @Builder
    public EmailResponse(boolean success, String message, int statusCode, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public static EmailResponse ok(String message) {
        return EmailResponse.builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static EmailResponse error(String message, int statusCode) {
        return EmailResponse.builder()
                .success(false)
                .message(message)
                .statusCode(statusCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

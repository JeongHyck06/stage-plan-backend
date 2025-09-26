package com.jackalone.stageplan.app.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailResponse {

    private boolean success;
    private String message;
    private String email;

    public static EmailResponse success(String message, String email) {
        return EmailResponse.builder()
                .success(true)
                .message(message)
                .email(email)
                .build();
    }

    public static EmailResponse failure(String message) {
        return EmailResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}

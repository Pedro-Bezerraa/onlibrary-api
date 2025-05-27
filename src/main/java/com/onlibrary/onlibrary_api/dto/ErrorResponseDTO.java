package com.onlibrary.onlibrary_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private int status;
    private String error;
    private LocalDateTime timestamp;
}

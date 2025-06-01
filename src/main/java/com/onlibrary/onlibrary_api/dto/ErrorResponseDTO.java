package com.onlibrary.onlibrary_api.dto;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        int status,
        String error,
        LocalDateTime timestamp
) { }

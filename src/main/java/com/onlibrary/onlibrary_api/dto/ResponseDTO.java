package com.onlibrary.onlibrary_api.dto;

public record ResponseDTO<T>(
        boolean success,
        String message,
        T data
) { }

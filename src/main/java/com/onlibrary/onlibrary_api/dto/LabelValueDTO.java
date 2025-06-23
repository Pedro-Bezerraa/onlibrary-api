package com.onlibrary.onlibrary_api.dto;

import java.util.UUID;

public record LabelValueDTO(
        String label,
        UUID value
) {
}

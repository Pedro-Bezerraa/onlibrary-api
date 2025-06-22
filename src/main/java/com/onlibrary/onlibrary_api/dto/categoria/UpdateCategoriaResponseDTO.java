package com.onlibrary.onlibrary_api.dto.categoria;

import java.util.UUID;

public record UpdateCategoriaResponseDTO(
        UUID id,
        String nome
) {
}

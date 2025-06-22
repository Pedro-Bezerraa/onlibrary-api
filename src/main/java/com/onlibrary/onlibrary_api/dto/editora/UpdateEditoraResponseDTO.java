package com.onlibrary.onlibrary_api.dto.editora;

import java.util.UUID;

public record UpdateEditoraResponseDTO(
        UUID id,
        String nome
) {
}


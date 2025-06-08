package com.onlibrary.onlibrary_api.dto.usuario;

import java.util.UUID;

public record LoginResponseDTO(
        String accessToken,
        UUID id,
        String username,
        String email
) {
}

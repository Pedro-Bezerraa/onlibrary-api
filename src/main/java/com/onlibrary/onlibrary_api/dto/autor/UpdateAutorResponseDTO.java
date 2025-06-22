package com.onlibrary.onlibrary_api.dto.autor;

import java.util.UUID;

public record UpdateAutorResponseDTO(
        UUID id,
        String nome
) { }

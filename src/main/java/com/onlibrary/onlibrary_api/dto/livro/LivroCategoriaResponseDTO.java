package com.onlibrary.onlibrary_api.dto.livro;

import java.util.UUID;

public record LivroCategoriaResponseDTO(
        UUID id,
        String titulo,
        String imagem
) {}
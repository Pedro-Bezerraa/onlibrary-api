package com.onlibrary.onlibrary_api.dto.livro;

import java.util.UUID;

public record LivroHomePageSearchDTO(
        UUID id,
        String titulo,
        String imagem
) {
}

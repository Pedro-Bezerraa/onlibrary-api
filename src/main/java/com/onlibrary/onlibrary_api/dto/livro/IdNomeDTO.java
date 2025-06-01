package com.onlibrary.onlibrary_api.dto.livro;

import java.util.UUID;

public record IdNomeDTO(
        UUID id,
        String nome
) {
}

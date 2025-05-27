package com.onlibrary.onlibrary_api.dto.biblioteca;

import java.util.UUID;

public record ExemplarRequestDTO(
        UUID livroId,
        String numeroTombo,
        Boolean disponivel,
        String estante,
        String prateleira,
        String setor,
        UUID bibliotecaId
) {
}

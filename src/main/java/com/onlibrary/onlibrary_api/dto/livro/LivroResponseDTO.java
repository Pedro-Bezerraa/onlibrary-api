package com.onlibrary.onlibrary_api.dto.livro;

import java.util.List;
import java.util.UUID;

public record LivroResponseDTO(
        UUID id,
        String isbn,
        String titulo,
        String descricao,
        Integer anoLancamento,
        List<UUID> autores,
        List<UUID> categorias,
        List<UUID> generos,
        List<UUID> editoras
) {
}
package com.onlibrary.onlibrary_api.dto.livro;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record AttLivroRequestDTO(
        String isbn,
        String titulo,
        String descricao,
        @JsonProperty("ano_lancamento") Integer anoLancamento,
        List<UUID> autores,
        List<UUID> categorias,
        List<UUID> generos,
        List<UUID> editoras
) {
}

package com.onlibrary.onlibrary_api.dto.livro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LivroRequestDTO(
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

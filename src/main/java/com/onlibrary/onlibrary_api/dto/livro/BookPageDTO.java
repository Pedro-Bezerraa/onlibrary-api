package com.onlibrary.onlibrary_api.dto.livro;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record BookPageDTO(
        UUID id,
        @JsonProperty("ISBN") String isbn,
        String titulo,
        String descricao,
        String imagem
) {}
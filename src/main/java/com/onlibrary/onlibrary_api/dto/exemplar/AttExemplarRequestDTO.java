package com.onlibrary.onlibrary_api.dto.exemplar;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AttExemplarRequestDTO(
        @JsonProperty("fk_id_livro") UUID livroId,
        @JsonProperty("numero_tombo") String numeroTombo,
        Boolean disponivel,
        String estante,
        String prateleira,
        String setor,
        String situacao
) {
}

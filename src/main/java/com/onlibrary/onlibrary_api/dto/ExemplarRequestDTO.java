package com.onlibrary.onlibrary_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ExemplarRequestDTO(
        @JsonProperty("fk_id_biblioteca") UUID bibliotecaId,
        @JsonProperty("fk_id_livro") UUID livroId,
        @JsonProperty("numero_tombo") String numeroTombo,
        String estante,
        String prateleira,
        String setor,
        String situacao
) {
}

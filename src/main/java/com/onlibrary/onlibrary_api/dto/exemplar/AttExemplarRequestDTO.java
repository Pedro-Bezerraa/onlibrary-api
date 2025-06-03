package com.onlibrary.onlibrary_api.dto.exemplar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;

import java.util.UUID;

public record AttExemplarRequestDTO(
        @JsonProperty("fk_id_livro") UUID livroId,
        @JsonProperty("numero_tombo") String numeroTombo,
        Boolean disponivel,
        String estante,
        String prateleira,
        String setor,
        SituacaoExemplar situacao
) {
}

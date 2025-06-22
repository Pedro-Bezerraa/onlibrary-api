package com.onlibrary.onlibrary_api.dto.multa;

import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record UpdateMultaRequestDTO(
        String motivo,
        SituacaoMulta situacao,
        @JsonProperty("fk_id_bibliotecario") UUID bibliotecarioId
) {
}
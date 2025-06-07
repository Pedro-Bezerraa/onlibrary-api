package com.onlibrary.onlibrary_api.dto.multa;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record MultaRequestDTO(
        @JsonProperty("fk_id_usuario") UUID usuarioId,
        @JsonProperty("fk_id_bibliotecario") UUID bibliotecarioId,
        @JsonProperty("fk_id_biblioteca") UUID bibliotecaId,
        String motivo
) {
}

package com.onlibrary.onlibrary_api.dto.contato;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContatoResponseDTO(
        UUID id,
        @JsonProperty("data_emissao") OffsetDateTime dataEmissao,
        @JsonProperty("usuario_id") UUID usuarioId,
        String conteudo
) {}
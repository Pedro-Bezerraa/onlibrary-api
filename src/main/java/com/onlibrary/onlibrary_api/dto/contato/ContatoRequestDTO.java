package com.onlibrary.onlibrary_api.dto.contato;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

// DTO para receber os dados do frontend
public record ContatoRequestDTO(
        @JsonProperty("fk_id_usuario") UUID usuarioId,
        String conteudo
) {}
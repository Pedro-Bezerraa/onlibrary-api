package com.onlibrary.onlibrary_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AttUsuarioBibliotecaDTO(
        @JsonProperty("fk_id_perfil_usuario") UUID perfilUsuarioId,
        @JsonProperty("tipo_usuario") String tipoUsuario,
        @JsonProperty("numero_matricula") String numeroMatricula
) {
}

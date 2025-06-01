package com.onlibrary.onlibrary_api.dto.usuarioBiblioteca;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record AttUsuarioBibliotecaRequestDTO(
        @JsonProperty("fk_id_perfil_usuario") UUID perfilUsuarioId,
        @JsonProperty("tipo_usuario") String tipoUsuario,
        @JsonProperty("numero_matricula") String numeroMatricula
) {
}

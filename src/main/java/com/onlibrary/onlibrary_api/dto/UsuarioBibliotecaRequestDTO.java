package com.onlibrary.onlibrary_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UsuarioBibliotecaRequestDTO(
        @JsonProperty("fk_id_biblioteca") UUID bibliotecaId,
        @JsonProperty("fk_id_usuario") UUID usuarioId,
        @JsonProperty("fk_id_perfil_usuario") UUID perfilUsuarioId,
        @JsonProperty("tipo_usuario") String tipoUsuario,
        @JsonProperty("numero_matricula") String numeroMatricula,
        String cpf
) {
}

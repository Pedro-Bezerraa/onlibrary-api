package com.onlibrary.onlibrary_api.dto.usuarioBiblioteca;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;

import java.util.UUID;

public record AttUsuarioBibliotecaRequestDTO(
        @JsonProperty("fk_id_perfil_usuario") UUID perfilUsuarioId,
        @JsonProperty("tipo_usuario") TipoUsuario tipoUsuario,
        @JsonProperty("numero_matricula") String numeroMatricula,
        ContaSituacao situacao
) {
}

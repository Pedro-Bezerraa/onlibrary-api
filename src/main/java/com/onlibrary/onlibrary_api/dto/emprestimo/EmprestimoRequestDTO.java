package com.onlibrary.onlibrary_api.dto.emprestimo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record EmprestimoRequestDTO(
        @JsonProperty("fk_id_biblioteca") UUID bibliotecaId,
        @JsonProperty("fk_id_bibliotecario") UUID bibliotecarioId,
        List<UUID> exemplares,
        @JsonProperty("fk_id_usuario_biblioteca") UUID usuarioBibliotecaId,
        String situacao
) {
}

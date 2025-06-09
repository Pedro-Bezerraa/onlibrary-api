package com.onlibrary.onlibrary_api.dto.usuario;

import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;

public record AttUsuarioResponseDTO(
        String nome,
        String sobrenome,
        String username,
        String email,
        String cpf,
        ContaSituacao situacao
) {
}

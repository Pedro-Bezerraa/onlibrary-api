package com.onlibrary.onlibrary_api.dto.usuario;

import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;

public record RegisterRequestDTO(
        String nome,
        String sobrenome,
        String username,
        String email,
        String senha,
        String cpf,
        ContaSituacao situacao
) { }

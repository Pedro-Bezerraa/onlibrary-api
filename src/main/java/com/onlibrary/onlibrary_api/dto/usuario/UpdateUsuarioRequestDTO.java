package com.onlibrary.onlibrary_api.dto.usuario;

public record UpdateUsuarioRequestDTO(
        String nome,
        String sobrenome,
        String username,
        String email,
        String cpf
) {
}

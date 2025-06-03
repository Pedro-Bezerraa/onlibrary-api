package com.onlibrary.onlibrary_api.dto.usuarioBiblioteca;

import java.util.UUID;

public record UsuarioBibliotecaResponseDTO(
        UUID id,
        UUID bibliotecaId,
        UUID usuarioId,
        UUID perfilUsuarioId,
        String tipoUsuario,
        String numeroMatricula,
        String cpf,
        String situacao
) { }

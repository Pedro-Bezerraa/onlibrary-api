package com.onlibrary.onlibrary_api.dto.usuario;

import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;

public record UpdateUsuarioRequestDTO(
        String nome,
        String sobrenome,
        String username,
        String email,
        String cpf,
        TipoUsuario tipoUsuario,
        ContaSituacao situacao
) {
}

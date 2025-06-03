package com.onlibrary.onlibrary_api.dto.perfilUsuario;

import java.util.UUID;

public record PerfilUsuarioResponseDTO(
        UUID id,
        String nome,
        Integer multaPadrao,
        Integer prazoDevolucaoPadrao,
        Integer prazoMultaPadrao,
        UUID bibliotecaId
) { }

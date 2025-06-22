package com.onlibrary.onlibrary_api.dto.biblioteca;

import java.util.UUID;

public record UpdateBibliotecaResponseDTO(
        UUID id,
        String nome,
        String telefone,
        String rua,
        Integer numero,
        String cep,
        boolean aplicacaoMulta,
        boolean reservaOnline,
        boolean aplicacaoBloqueio
) {
}

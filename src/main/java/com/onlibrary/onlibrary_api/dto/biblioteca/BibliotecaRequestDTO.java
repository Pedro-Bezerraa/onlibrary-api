package com.onlibrary.onlibrary_api.dto.biblioteca;

public record BibliotecaRequestDTO(
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

package com.onlibrary.onlibrary_api.dto.biblioteca;

public record CreateBibliotecaDTO(
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

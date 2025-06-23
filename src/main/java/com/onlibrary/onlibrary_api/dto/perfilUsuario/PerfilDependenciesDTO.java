package com.onlibrary.onlibrary_api.dto.perfilUsuario;

public record PerfilDependenciesDTO(
        String nome,
        Integer multa_padrao,
        Integer prazo_devolucao_padrao,
        Integer prazo_multa_padrao
) {
}

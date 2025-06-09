package com.onlibrary.onlibrary_api.dto.biblioteca;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BibliotecaRequestDTO(
        String nome,
        String telefone,
        String rua,
        Integer numero,
        String cep,
        @JsonProperty("aplicacao_multa") boolean aplicacaoMulta,
        @JsonProperty("reserva_online") boolean reservaOnline,
        @JsonProperty("aplicacao_bloqueio") boolean aplicacaoBloqueio
) {
}

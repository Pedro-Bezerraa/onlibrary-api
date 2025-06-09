package com.onlibrary.onlibrary_api.dto.biblioteca;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AttBibliotecaRequestDTO(
        String nome,
        String telefone,
        String rua,
        Integer numero,
        String cep,
        @JsonProperty("aplicacao_multa") Boolean aplicacaoMulta,
        @JsonProperty("reserva_online") Boolean reservaOnline,
        @JsonProperty("aplicacao_bloqueio") Boolean aplicacaoBloqueio
) {
}

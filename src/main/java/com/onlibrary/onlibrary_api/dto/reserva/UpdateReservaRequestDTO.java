package com.onlibrary.onlibrary_api.dto.reserva;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateReservaRequestDTO(
        @JsonProperty("data_retirada") LocalDate dataRetirada,
        @JsonProperty("fk_id_bibliotecario") UUID bibliotecarioId,
        SituacaoReserva situacao
) {
}

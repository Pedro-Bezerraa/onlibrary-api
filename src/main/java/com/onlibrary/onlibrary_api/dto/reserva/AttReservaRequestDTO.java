package com.onlibrary.onlibrary_api.dto.reserva;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.UUID;

public record AttReservaRequestDTO(
        @JsonProperty("data_retirada") LocalDate dataRetirada,
        @JsonProperty("fk_id_bibliotecario") UUID bibliotecarioId
) {
}

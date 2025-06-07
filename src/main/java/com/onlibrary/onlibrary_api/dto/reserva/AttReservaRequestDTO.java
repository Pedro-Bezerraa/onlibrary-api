package com.onlibrary.onlibrary_api.dto.reserva;

import java.time.LocalDate;
import java.util.UUID;

public record AttReservaRequestDTO(
        LocalDate dataRetirada,
        UUID bibliotecarioId
) {
}

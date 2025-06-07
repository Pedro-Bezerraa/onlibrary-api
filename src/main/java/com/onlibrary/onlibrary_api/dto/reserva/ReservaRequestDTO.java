package com.onlibrary.onlibrary_api.dto.reserva;

import com.onlibrary.onlibrary_api.model.enums.TipoReserva;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservaRequestDTO(
        UUID bibliotecaId,
        UUID usuarioId,
        UUID bibliotecarioId,
        UUID livroId,
        TipoReserva tipoReserva,
        BigDecimal quantidade
) {
}

package com.onlibrary.onlibrary_api.dto.reserva;

import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import com.onlibrary.onlibrary_api.model.enums.TipoReserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReservaResponseDTO(
        UUID reservaId,
        UUID bibliotecaId,
        UUID usuarioid,
        UUID bibliotecarioId,
        LocalDate dataEmissao,
        LocalDate dataRetirada,
        SituacaoReserva situacaoReserva,
        TipoReserva tipoReserva,
        BigDecimal qntdTotal,
        BigDecimal qntdPendente,
        UUID livroId
) {
}

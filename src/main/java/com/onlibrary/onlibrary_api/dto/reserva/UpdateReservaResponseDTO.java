package com.onlibrary.onlibrary_api.dto.reserva;

import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import com.onlibrary.onlibrary_api.model.enums.TipoReserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateReservaResponseDTO(
        UUID reservaId,
        UUID bibliotecaId,
        UUID usuarioid,
        LocalDate dataEmissao,
        LocalDate dataRetirada,
        SituacaoReserva situacaoReserva,
        TipoReserva tipoReserva,
        BigDecimal qntdTotal,
        BigDecimal qntdPendente,
        UUID livroId
) {
}

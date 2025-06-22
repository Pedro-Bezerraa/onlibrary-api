package com.onlibrary.onlibrary_api.dto.multa;

import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateMultaResponseDTO(
        UUID id,
        UUID usuarioId,
        UUID bibliotecarioId,
        Integer valor,
        LocalDate dataEmissao,
        LocalDate dataVencimento,
        SituacaoMulta situacaoMulta,
        String motivo,
        UUID bibliotecaId,
        UUID emprestimoId
) {
}

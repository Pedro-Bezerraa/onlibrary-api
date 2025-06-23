package com.onlibrary.onlibrary_api.dto.multa;

import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;

import java.time.LocalDate;
import java.util.UUID;

public record MultaDependenciesDTO(
        Integer valor,
        String motivo,
        LocalDate data_vencimento,
        SituacaoMulta situacao,
        LabelValue<UUID> usuarios_biblioteca
) {
    public record LabelValue<T>(String label, T value) {}
}

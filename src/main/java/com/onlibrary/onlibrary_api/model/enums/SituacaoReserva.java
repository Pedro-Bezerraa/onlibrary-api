package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoReserva {
    PENDENTE,
    ATENDIDO_PARCIALMENTE,
    ATENDIDO_COMPLETAMENTE,
    CONCLUIDO;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static SituacaoReserva from(String value) {
        return SituacaoReserva.valueOf(value.toUpperCase());
    }
}

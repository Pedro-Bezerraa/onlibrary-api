package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoEmprestimo {
    CONCLUIDO,
    PENDENTE,
    VENCIDO,
    CANCELADO;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static SituacaoEmprestimo from(String value) {
        return SituacaoEmprestimo.valueOf(value.toUpperCase());
    }
}

package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoMulta {
    CONCLUIDO,
    PENDENTE,
    CANCELADO;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static SituacaoMulta from(String value) {
        return SituacaoMulta.valueOf(value.toUpperCase());
    }
}

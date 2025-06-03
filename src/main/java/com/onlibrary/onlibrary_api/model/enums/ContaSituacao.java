package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ContaSituacao {
    ATIVO,
    BLOQUEADO;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ContaSituacao from(String value) {
        return ContaSituacao.valueOf(value.toUpperCase());
    }
}

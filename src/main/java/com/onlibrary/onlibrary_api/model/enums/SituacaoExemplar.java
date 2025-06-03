package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SituacaoExemplar {
    DISPONIVEL,
    INDISPONIVEL,
    RESERVADO,
    EMPRESTADO;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static SituacaoExemplar from(String value) {
        return SituacaoExemplar.valueOf(value.toUpperCase());
    }
}

package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoNotificacao {
    DATA;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static TipoNotificacao from(String value) {
        return TipoNotificacao.valueOf(value.toUpperCase());
    }
}

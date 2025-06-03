package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoReserva {
    ONLINE,
    FISICO;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static TipoReserva from(String value) {
        return TipoReserva.valueOf(value.toUpperCase());
    }
}

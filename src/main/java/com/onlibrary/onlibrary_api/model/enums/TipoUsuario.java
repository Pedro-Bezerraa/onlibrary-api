package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoUsuario {
    ADMIN,
    ADMIN_MASTER,
    COMUM;

    @JsonValue
    public String toLower() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static TipoUsuario from(String value) {
        return TipoUsuario.valueOf(value.toUpperCase());
    }
}

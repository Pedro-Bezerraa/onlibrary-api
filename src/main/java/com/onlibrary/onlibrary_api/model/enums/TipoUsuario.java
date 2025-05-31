package com.onlibrary.onlibrary_api.model.enums;

import com.onlibrary.onlibrary_api.exception.BusinessException;

public enum TipoUsuario {
    ADMIN,
    COMUM;

    public static TipoUsuario fromString(String value) {
        try {
            return TipoUsuario.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException("Situação inválida. Use ADMIN ou COMUM.");
        }
    }
}

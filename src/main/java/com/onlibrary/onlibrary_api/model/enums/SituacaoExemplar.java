package com.onlibrary.onlibrary_api.model.enums;

import com.onlibrary.onlibrary_api.exception.BusinessException;

public enum SituacaoExemplar {
    DISPONIVEL,
    INDISPONIVEL,
    RESERVADO,
    EMPRESTADO;

    public static SituacaoExemplar fromString(String value) {
        try {
            return SituacaoExemplar.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException("Situação inválida. Use DISPONIVEL ou INDISPONIVEL.");
        }
    }
}

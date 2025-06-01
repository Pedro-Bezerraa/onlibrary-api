package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.onlibrary.onlibrary_api.exception.BusinessException;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SituacaoEmprestimo {
    CONCLUIDO,
    PENDENTE,
    CANCELADO;

    @JsonCreator
    public static SituacaoEmprestimo fromString(String value) {
        try {
            return SituacaoEmprestimo.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException("Situação inválida. Use DISPONIVEL ou INDISPONIVEL.");
        }
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}

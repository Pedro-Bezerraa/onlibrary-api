package com.onlibrary.onlibrary_api.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SituacaoEmprestimo {
    CONCLUIDO,
    PENDENTE,
    CANCELADO
}

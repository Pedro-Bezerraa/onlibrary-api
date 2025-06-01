package com.onlibrary.onlibrary_api.dto.emprestimo;

import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;

import java.time.LocalDate;

public record AttEmprestimoRequestDTO(
        SituacaoEmprestimo situacao,
        LocalDate dataDevolucao
) {
}

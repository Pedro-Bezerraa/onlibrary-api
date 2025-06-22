package com.onlibrary.onlibrary_api.dto.emprestimo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;

import java.time.LocalDate;

public record UpdateEmprestimoRequestDTO(
        SituacaoEmprestimo situacao,
        @JsonProperty("data_devolucao") LocalDate dataDevolucao
) {
}

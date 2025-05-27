package com.onlibrary.onlibrary_api.dto.biblioteca;

import java.time.LocalDate;

public record EmprestimoRequestDTO(
        String situacao,
        LocalDate dataDevolucao
) {
}

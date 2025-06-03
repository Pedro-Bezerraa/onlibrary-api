package com.onlibrary.onlibrary_api.dto.emprestimo;

import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record EmprestimoResponseDTO(
        UUID id,
        LocalDate dataEmissao,
        LocalDate dataDevolucao,
        SituacaoEmprestimo situacaoEmprestimo,
        UUID usuarioBibliotecaId,
        UUID bibliotecarioId,
        UUID bibliotecaId,
        List<UUID> exemplares
) {
}

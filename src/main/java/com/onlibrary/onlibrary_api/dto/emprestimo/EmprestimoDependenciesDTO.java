package com.onlibrary.onlibrary_api.dto.emprestimo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record EmprestimoDependenciesDTO(
        LabelValue<String> situacao,
        LocalDate data_devolucao,
        List<LabelValue<UUID>> exemplares_do_emprestimo,
        LabelValue<UUID> usuario_do_emprestimo
) {
    public record LabelValue<T>(String label, T value) {}
}
package com.onlibrary.onlibrary_api.dto.exemplar;

import com.onlibrary.onlibrary_api.dto.LabelValueDTO;

import java.util.List;

public record ExemplarDependenciesDTO(
        String numero_tombo,
        LabelValue<String> situacao,
        LabelValueDTO livro_atual,
        String setor,
        String prateleira,
        String estante,
        List<LabelValueDTO> todos_os_livros_da_biblioteca
) {
    public record LabelValue<T>(String label, T value) {}

}

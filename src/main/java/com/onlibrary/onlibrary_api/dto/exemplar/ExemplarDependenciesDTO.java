package com.onlibrary.onlibrary_api.dto.exemplar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.dto.LabelValueDTO;

import java.util.List;

public record ExemplarDependenciesDTO(
        String numero_tombo,
        LabelValue<String> situacao,
        LabelValueDTO livros_biblioteca,
        String setor,
        String prateleira,
        String estante,
        @JsonProperty("livros_biblioteca") List<LabelValueDTO> todos_os_livros_da_biblioteca
) {
    public record LabelValue<T>(String label, T value) {}

}

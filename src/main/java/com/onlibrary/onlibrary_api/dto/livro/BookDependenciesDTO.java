package com.onlibrary.onlibrary_api.dto.livro;

import com.onlibrary.onlibrary_api.dto.LabelValueDTO;

import java.util.List;

public record BookDependenciesDTO(
        List<LabelValueDTO> autores,
        List<LabelValueDTO> categorias,
        List<LabelValueDTO> generos,
        List<LabelValueDTO> editoras
) {
}

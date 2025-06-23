package com.onlibrary.onlibrary_api.dto.biblioteca;

import com.onlibrary.onlibrary_api.dto.LabelValueDTO;

import java.util.List;

public record LibraryUserDependenciesDTO(
        List<LabelValueDTO> usuarios,
        List<LabelValueDTO> perfis_biblioteca
) {
}

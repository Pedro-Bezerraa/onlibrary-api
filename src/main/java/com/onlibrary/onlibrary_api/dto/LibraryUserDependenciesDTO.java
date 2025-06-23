package com.onlibrary.onlibrary_api.dto;

import java.util.List;

public record LibraryUserDependenciesDTO(
        List<LabelValueDTO> usuarios,
        List<LabelValueDTO> perfis_biblioteca
) {
}

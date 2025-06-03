package com.onlibrary.onlibrary_api.dto.exemplar;

import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;

import java.util.UUID;

public record ExemplarResponseDTO(
        UUID id,
        String numeroTombo,
        String setor,
        String prateleira,
        String estante,
        SituacaoExemplar situacaoExemplar,
        UUID livroId,
        UUID bibliotecaId
) {
}

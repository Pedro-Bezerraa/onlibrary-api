package com.onlibrary.onlibrary_api.dto.exemplar;

import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import java.util.UUID;

public record ExemplarStatusDTO(
        UUID id,
        SituacaoExemplar situacao
) {}
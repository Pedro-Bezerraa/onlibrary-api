package com.onlibrary.onlibrary_api.dto.reserva;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.enums.TipoReserva;

import java.math.BigDecimal;
import java.util.UUID;

public record ReservaRequestDTO(
        @JsonProperty("fk_id_biblioteca") UUID bibliotecaId,
        @JsonProperty("fk_id_usuario") UUID usuarioId,
        @JsonProperty("fk_id_bibliotecario") UUID bibliotecarioId,
        @JsonProperty("fk_id_livro") UUID livroId,
        TipoReserva tipo,
        @JsonProperty("quantidade_total") BigDecimal quantidade
) {
}

package com.onlibrary.onlibrary_api.dto.perfilUsuario;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AttPerfilUsuarioRequestDTO(
        String nome,
        @JsonProperty("multa_padrao") Integer multaPadrao,
        @JsonProperty("prazo_devolucao_padrao") Integer prazoDevolucaoPadrao,
        @JsonProperty("prazo_multa_padrao") Integer prazoMultaPadrao
) {
}

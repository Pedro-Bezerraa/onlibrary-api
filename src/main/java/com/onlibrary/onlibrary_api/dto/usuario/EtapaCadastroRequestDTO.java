package com.onlibrary.onlibrary_api.dto.usuario;

import java.util.Map;

public record EtapaCadastroRequestDTO(
        int etapa,
        Map<String, String> dados
) { }

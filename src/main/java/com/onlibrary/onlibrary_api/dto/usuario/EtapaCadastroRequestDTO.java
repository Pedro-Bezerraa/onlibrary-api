package com.onlibrary.onlibrary_api.dto.usuario;

import lombok.Data;

import java.util.Map;

@Data
public class EtapaCadastroRequestDTO {
    private int etapa;
    private Map<String, String> dados;
}

package com.onlibrary.onlibrary_api.dto.usuarioBiblioteca;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public record UsuarioBibliotecaDependenciesDTO(
        String numero_matricula,
        String cpf,
        LabelValue<String> tipo_usuario,
        LabelValue<String> situacao,
        @JsonProperty("usuarios") LabelValue<String> usuario, // <-- CORREÇÃO: Renomeado para "usuarios"
        @JsonProperty("perfis_biblioteca") LabelValue<UUID> perfil_atual// <-- CORREÇÃO: Renomeado para "perfis_biblioteca"
        // O campo 'todos_os_perfis_da_biblioteca' foi removido
) {
    public record LabelValue<T>(String label, T value) {}
}
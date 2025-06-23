package com.onlibrary.onlibrary_api.dto.usuarioBiblioteca;

import java.util.List;
import java.util.UUID;

public record UsuarioBibliotecaDependenciesDTO(
        String numero_matricula,
        String cpf,
        LabelValue<String> tipo_usuario,
        LabelValue<String> situacao,
        LabelValue<String> usuario,
        LabelValue<UUID> perfil_atual,
        List<LabelValue<UUID>> todos_os_perfis_da_biblioteca
) {
    public record LabelValue<T>(String label, T value) {}

}

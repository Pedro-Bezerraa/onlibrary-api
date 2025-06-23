package com.onlibrary.onlibrary_api.dto.biblioteca;

public record BibliotecaDependenciesDTO(
        String nome,
        String telefone,
        String rua,
        Integer numero,
        String cep,
        BooleanLabelValue aplicacao_multa,
        BooleanLabelValue reserva_online,
        BooleanLabelValue aplicacao_bloqueio
) {
    public record BooleanLabelValue(String label, boolean value) {}
}

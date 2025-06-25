package com.onlibrary.onlibrary_api.dto.reserva;

import com.onlibrary.onlibrary_api.dto.LabelValueDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReservaDependenciesDTO(
        BigDecimal quantidade_total,
        LabelValue<String> situacao,
        LocalDate data_retirada,
        LabelValueDTO usuarios_biblioteca,
        LabelValueDTO livros_biblioteca
//        List<LabelValueDTO> todos_os_usuarios,
//        List<LabelValueDTO> todos_os_livros_da_biblioteca
) {
    public record LabelValue<T>(String label, T value) {}
}

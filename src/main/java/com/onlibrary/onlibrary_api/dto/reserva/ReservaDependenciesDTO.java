package com.onlibrary.onlibrary_api.dto.reserva;

import com.onlibrary.onlibrary_api.dto.LabelValueDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReservaDependenciesDTO(
        BigDecimal quantidade_total,
        LabelValue<String> situacao,
        LocalDate data_retirada,
        List<LabelValueDTO> usuarios_biblioteca,
        List<LabelValueDTO> livros_biblioteca
) {
    public record LabelValue<T>(String label, T value) {}
}

package com.onlibrary.onlibrary_api.dto.graficos;

import java.time.LocalDate;

public record ChartWeekDataDTO(
        Long data,
        String dia_semana,
        LocalDate data_emissao
) {}
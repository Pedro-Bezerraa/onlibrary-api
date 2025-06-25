package com.onlibrary.onlibrary_api.dto.graficos;

import java.util.List;

public record ChartDataResponseDTO(
        List<ChartWeekDataDTO> weekData,
        List<ChartLastDataDTO> lastData
) {}
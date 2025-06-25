package com.onlibrary.onlibrary_api.repository.custom;

import com.onlibrary.onlibrary_api.dto.graficos.ChartLastDataDTO;
import com.onlibrary.onlibrary_api.dto.graficos.ChartWeekDataDTO;

import java.util.List;
import java.util.UUID;

public interface ChartRepository {
    List<ChartWeekDataDTO> getWeekData(String nomeTabela, UUID bibliotecaId);
    List<ChartLastDataDTO> getLastData(String nomeTabela, UUID bibliotecaId);
}
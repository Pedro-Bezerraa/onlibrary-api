package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.ContagemResponseDTO;
import com.onlibrary.onlibrary_api.dto.graficos.ChartDataResponseDTO;
import com.onlibrary.onlibrary_api.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {
    private final DataService dataService;

    @GetMapping("/graficos")
    public ResponseEntity<ResponseDTO<ChartDataResponseDTO>> getChartData(
            @RequestParam("nome_tabela") String nomeTabela,
            @RequestParam(name = "id_biblioteca", required = false) UUID bibliotecaId) {

        ChartDataResponseDTO chartData = dataService.getChartData(nomeTabela, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dados do gráfico recuperados com sucesso.", chartData));
    }

    @GetMapping("/group")
    public ResponseEntity<ResponseDTO<Object>> getGroupData(
            @RequestParam("type") String type,
            @RequestParam(name = "id_biblioteca", required = false) UUID bibliotecaId) {

        Object groupData = dataService.getGroupData(type, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dados agrupados recuperados com sucesso.", groupData));
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseDTO<ContagemResponseDTO>> getCount(
            @RequestParam("type") String type,
            @RequestParam(value = "id_biblioteca", required = false) UUID bibliotecaId,
            @RequestParam(name = "id_usuario", required = false) UUID usuarioId) {

        ContagemResponseDTO contagem = dataService.getCount(type, bibliotecaId, usuarioId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Contagem realizada com sucesso.", contagem));
    }

    @GetMapping("/dados")
    public ResponseEntity<ResponseDTO<List<?>>> getData(
            @RequestParam("type") String type,
            @RequestParam(name = "id_biblioteca", required = false) UUID bibliotecaId) {

        if (type.startsWith("library_") || List.of("exemplary", "account", "loan", "amerce", "reserve", "online_reserve").contains(type)) {
            if (bibliotecaId == null) {
                return ResponseEntity.badRequest().body(new ResponseDTO<>(false, "O parâmetro 'id_biblioteca' é obrigatório para este tipo de dado.", null));
            }
        }

        List<?> data = dataService.getData(type, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dados recuperados com sucesso.", data));
    }
}

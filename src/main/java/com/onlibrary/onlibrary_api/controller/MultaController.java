package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.multa.MultaDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.multa.UpdateMultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.UpdateMultaResponseDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaResponseDTO;
import com.onlibrary.onlibrary_api.model.views.VwTableMulta;
import com.onlibrary.onlibrary_api.service.MultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/multa")
@RequiredArgsConstructor
public class MultaController {
    private final MultaService multaService;

    @PostMapping("/criar-multa")
    public ResponseEntity<?> criarMulta(
            @RequestBody MultaRequestDTO dto
    ) {
        MultaResponseDTO multa = multaService.cadastrarMulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Multa realizada com sucesso.", multa));
    }

    @PutMapping("/atualizar-multa/{multaId}")
    public ResponseEntity<?> atualizarMulta(
            @PathVariable UUID multaId,
            @RequestBody UpdateMultaRequestDTO dto
    ) {
        UpdateMultaResponseDTO multa = multaService.atualizarMulta(multaId, dto);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Multa atualizada com sucesso.", multa));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarMulta(@PathVariable UUID id) {
        multaService.deletarMulta(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Multa marcada como deletada com sucesso.", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<VwTableMulta>>> searchMultas(
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter,
            @RequestParam("id_biblioteca") UUID bibliotecaId) {
        List<VwTableMulta> result = multaService.searchMultas(value, filter, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de multas realizada com sucesso.", result));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<MultaDependenciesDTO>> getMultaDependencies(@PathVariable UUID id) {
        MultaDependenciesDTO dependencies = multaService.getMultaDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências da multa recuperadas com sucesso.", dependencies));
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<ResponseDTO<List<VwTableMulta>>> getMultasByUser(@PathVariable UUID usuarioId) {
        List<VwTableMulta> multas = multaService.getMultasByUsuario(usuarioId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Multas do usuário recuperadas com sucesso.", multas));
    }
}

package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.UpdateExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarResponseDTO;
import com.onlibrary.onlibrary_api.service.ExemplarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/exemplar")
@RequiredArgsConstructor
@Slf4j
public class ExemplarController {
    private final ExemplarService exemplarService;

    @PostMapping("/criar-exemplar")
    public ResponseEntity<?> criarExemplar(@RequestBody ExemplarRequestDTO dto) {
        ExemplarResponseDTO exemplar = exemplarService.criarExemplar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Exemplar criado com sucesso!", exemplar));
    }

    @PutMapping("/atualizar-exemplar/{id}")
    public ResponseEntity<?> atualizarExemplar(@PathVariable UUID id, @RequestBody UpdateExemplarRequestDTO dto) {
        ExemplarResponseDTO exemplarAtualizado = exemplarService.atualizarExemplar(id, dto);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Exemplar atualizado com sucesso!", exemplarAtualizado));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarExemplar(@PathVariable UUID id) {
        exemplarService.deletarExemplar(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Exemplar marcado como deletado com sucesso.", null));
    }
}

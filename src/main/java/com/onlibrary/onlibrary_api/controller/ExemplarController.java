package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.AttExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.ExemplarRequestDTO;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.ExemplarService;
import jakarta.persistence.EntityNotFoundException;
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
        try {
            exemplarService.criarExemplar(dto);
            return ResponseEntity.ok("Exemplar criado com sucesso");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-exemplar/{id}")
    public ResponseEntity<?> atualizarExemplar(@PathVariable UUID id, @RequestBody AttExemplarRequestDTO dto) {

        try {
            exemplarService.atualizarExemlar(id, dto);
            return ResponseEntity.ok("Exemplar atualizado com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao atualizar exemplar", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar exemplar.");
        }
    }
}

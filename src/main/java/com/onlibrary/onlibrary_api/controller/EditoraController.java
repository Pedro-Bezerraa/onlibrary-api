package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.editora.EditoraRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.EditoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/editora")
@RequiredArgsConstructor
public class EditoraController {
    private final EditoraService editoraService;

    @PostMapping("/criar-editora")
    public ResponseEntity<?> criarEditora(@RequestBody EditoraRequestDTO dto) {
        try {
            editoraService.criarEditora(dto);
            return ResponseEntity.ok("Editora criado com sucesso");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-editora/{id}")
    public ResponseEntity<?> atualizarEditora(@PathVariable UUID id, @RequestBody EditoraRequestDTO dto) {
        try {
            editoraService.atualizar(id, dto);
            return ResponseEntity.ok("Categoria atualizado com sucesso");
        } catch (InvalidCredentialsException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

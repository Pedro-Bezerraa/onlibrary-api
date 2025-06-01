package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.categoria.CategoriaRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping("/criar-categoria")
    public ResponseEntity<?> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        try {
            categoriaService.criarCategoria(dto);
            return ResponseEntity.ok("Categoria criada com sucesso");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-categoria/{id}")
    public ResponseEntity<?> atualizarCategoria(@PathVariable UUID id, @RequestBody CategoriaRequestDTO dto) {
        try {
            categoriaService.atualizarCategoria(id, dto);
            return ResponseEntity.ok("Categoria atualizado com sucesso");
        } catch (InvalidCredentialsException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
